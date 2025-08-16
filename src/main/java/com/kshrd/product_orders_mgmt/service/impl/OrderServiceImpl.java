package com.kshrd.product_orders_mgmt.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kshrd.product_orders_mgmt.exception.NotFoundException;
import com.kshrd.product_orders_mgmt.model.dto.request.OrderItemRequest;
import com.kshrd.product_orders_mgmt.model.dto.response.OrderResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PagedResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PaginationInfo;
import com.kshrd.product_orders_mgmt.model.entity.Customer;
import com.kshrd.product_orders_mgmt.model.entity.Order;
import com.kshrd.product_orders_mgmt.model.entity.OrderItem;
import com.kshrd.product_orders_mgmt.model.entity.OrderItemId;
import com.kshrd.product_orders_mgmt.model.entity.Product;
import com.kshrd.product_orders_mgmt.model.enumeration.OrderProperty;
import com.kshrd.product_orders_mgmt.model.enumeration.OrderStatus;
import com.kshrd.product_orders_mgmt.repository.CustomerRepository;
import com.kshrd.product_orders_mgmt.repository.OrderRepository;
import com.kshrd.product_orders_mgmt.repository.ProductRepository;
import com.kshrd.product_orders_mgmt.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    private Order findEntityById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(
                        "Order with ID: " + orderId + " doesn't exist"));
    }

    @Override
    public OrderResponse findById(Long orderId) {
        return findEntityById(orderId).toResponse();
    }

    @Override
    public OrderResponse create(Long customerId, List<OrderItemRequest> itemRequests) {
        Customer foundCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException(
                        "Customer with ID: " + customerId + " doesn't exist"));

        // merge in case there is duplicate productId in the request
        Map<Long, Integer> itemRequestsMap = new HashMap<>();
        for (OrderItemRequest orderItemRequest : itemRequests) {
            Integer oldQuantity = itemRequestsMap.get(orderItemRequest.getProductId());
            itemRequestsMap.put(orderItemRequest.getProductId(),
                    (oldQuantity == null ? 0 : oldQuantity) + orderItemRequest.getQuantity());
        }
        List<OrderItemRequest> mergedItemRequests = itemRequestsMap.entrySet().stream()
                .map(entry -> new OrderItemRequest(entry.getKey(), entry.getValue())).toList();

        Order newOrder = Order.builder().customer(foundCustomer).totalAmount(BigDecimal.ZERO).build();

        // get a list of productId
        List<Long> productIds = mergedItemRequests.stream().map((item) -> item.getProductId()).toList();

        // get a list of products in one query
        List<Product> foundProducts = productRepository.findByProductIdIn(productIds);

        // verify Ids that doesn't exist
        List<Long> notFoundProductIds = productIds.stream().filter(
                productId -> foundProducts.stream()
                        .noneMatch(product -> product.getProductId().equals(productId)))
                .toList();

        // throw exception if one or more productId is not found
        if (!notFoundProductIds.isEmpty()) {
            String notFoundIdString = notFoundProductIds.stream().map(String::valueOf)
                    .collect(Collectors.joining(", "));
            throw new NotFoundException("Product with ID: " + notFoundIdString + " doesn't exist");
        }

        // if all product are found, it should match to itemRequest by index
        List<OrderItem> newOrderItems = IntStream.range(0, mergedItemRequests.size()).mapToObj(i -> {
            OrderItemId orderItemId = new OrderItemId(null,
                    foundProducts.get(i).getProductId());
            return OrderItem.builder().id(orderItemId).order(newOrder).product(foundProducts.get(i))
                    .quantity(mergedItemRequests.get(i).getQuantity()).build();
        })
                .toList();

        newOrder.setOrderItems(newOrderItems);
        newOrder.setTotalAmount(newOrderItems.stream()
                .map(item -> item.getProduct().getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Order savedOrder = orderRepository.save(newOrder);
        return savedOrder.toResponse();
    }

    @Override
    public PagedResponse<OrderResponse> findByCustomerId(Long customerId, Integer page, Integer size,
            OrderProperty orderProperty, Direction direction) {
        customerRepository.findById(customerId).orElseThrow(
                () -> new NotFoundException("Customer with ID: " + customerId + " doesn't exist"));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, orderProperty.getValue()));

        Page<Order> orders = orderRepository.findByCustomerId(customerId, pageable);
        return PagedResponse.<OrderResponse>builder()
                .items(orders.getContent().stream().map(o -> o.toResponse()).toList())
                .pagination(new PaginationInfo(orders)).build();
    }

    @Override
    public OrderResponse updateStatusById(Long orderId, OrderStatus status) {
        Order foundOrder = findEntityById(orderId);
        foundOrder.setStatus(status);
        return orderRepository.save(foundOrder).toResponse();
    }

    @Override
    public OrderResponse deleteById(Long orderId) {
        Order foundOrder = findEntityById(orderId);
        orderRepository.delete(foundOrder);
        return foundOrder.toResponse();
    }
}
