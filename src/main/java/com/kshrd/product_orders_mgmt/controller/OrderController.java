package com.kshrd.product_orders_mgmt.controller;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kshrd.product_orders_mgmt.model.dto.request.OrderItemRequest;
import com.kshrd.product_orders_mgmt.model.dto.response.ApiResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.OrderResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PagedResponse;
import com.kshrd.product_orders_mgmt.model.enumeration.OrderProperty;
import com.kshrd.product_orders_mgmt.model.enumeration.OrderStatus;
import com.kshrd.product_orders_mgmt.service.OrderService;
import com.kshrd.product_orders_mgmt.utils.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{order-id}")
    @Operation(summary = "Get order by ID", description = "Fetches a single order, including its line items and status, using the provided order ID.")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable("order-id") Long orderId) {
        OrderResponse orderResponse = orderService.findById(orderId);
        return ResponseUtils.createResponse("Fetch order with ID: " + orderId + " successfully", HttpStatus.FOUND,
                orderResponse);
    }

    @PostMapping("/{customer-id}")
    @Operation(summary = "Create Order by Customer ID", description = "Creates one new order for the given customer using a non-empty list of ```OrderRequest``` items (each item represents an order line). Returns the created order with calculated totals.")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrderByCustomerId(
            @PathVariable("customer-id") Long customerId,
            @RequestBody List<OrderItemRequest> itemRequests) {
        OrderResponse orderResponse = orderService.create(customerId, itemRequests);
        return ResponseUtils.createResponse("Created new order successfully", HttpStatus.CREATED, orderResponse);
    }

    @GetMapping("/customers/{customer-id}")
    @Operation(summary = "List orders by customer (paginated)", description = "Retrieves a paginated list of orders for the specified customer. Supports page/size and sorting by ```OrderProperty``` with ```Sort.Direction```.")
    public ResponseEntity<ApiResponse<PagedResponse<OrderResponse>>> getOrderByCustomerId(
            @PathVariable("customer-id") Long customerId,
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestParam(defaultValue = "ID") OrderProperty orderProperty,
            @RequestParam(defaultValue = "ASC") Direction direction) {
        PagedResponse<OrderResponse> orderResponses = orderService.findByCustomerId(customerId, page, size,
                orderProperty, direction);
        return ResponseUtils.createResponse("Fetch Orders for customer with ID: " + customerId + " successfully",
                HttpStatus.OK, orderResponses);
    }

    @PutMapping("/{order-id}/status")
    @Operation(summary = "Update order status by ID", description = "Updates the status of an existing order using the provided order ID and OrderStatus value (e.g., ```PENDING```, ```PAID```, ```SHIPPED```, ```CANCELLED```).")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(@PathVariable("order-id") Long orderId,
            @RequestParam OrderStatus status) {
        OrderResponse orderResponse = orderService.updateStatusById(orderId, status);
        return ResponseUtils.createResponse(
                "Change order with ID: " + orderId + " to status '" + status.toString() + "' successfully",
                HttpStatus.OK, orderResponse);
    }

    @DeleteMapping("/{order-id}")
    @Operation(summary = "Delete order by ID", description = "Deletes an order from the system using the specified order ID.")
    public ResponseEntity<ApiResponse<OrderResponse>> deleteOrderById(@PathVariable("order-id") Long orderId) {
        OrderResponse orderResponse = orderService.deleteById(orderId);
        return ResponseUtils.createResponse("Delete order with ID: " + orderId + " successfully", HttpStatus.OK,
                orderResponse);
    }
}
