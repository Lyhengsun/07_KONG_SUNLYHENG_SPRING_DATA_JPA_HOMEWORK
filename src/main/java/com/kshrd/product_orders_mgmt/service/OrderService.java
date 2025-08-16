package com.kshrd.product_orders_mgmt.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.kshrd.product_orders_mgmt.model.dto.request.OrderItemRequest;
import com.kshrd.product_orders_mgmt.model.dto.response.OrderResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PagedResponse;
import com.kshrd.product_orders_mgmt.model.enumeration.OrderProperty;
import com.kshrd.product_orders_mgmt.model.enumeration.OrderStatus;

public interface OrderService {

    OrderResponse findById(Long orderId);

    OrderResponse create(Long customerId, List<OrderItemRequest> itemRequests);

    PagedResponse<OrderResponse> findByCustomerId(Long customerId, Integer page, Integer size,
            OrderProperty orderProperty, Direction direction);

    OrderResponse updateStatusById(Long orderId, OrderStatus status);

    OrderResponse deleteById(Long orderId);

}
