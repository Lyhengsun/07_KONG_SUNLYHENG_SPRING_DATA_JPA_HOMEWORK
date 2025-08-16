package com.kshrd.product_orders_mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kshrd.product_orders_mgmt.model.entity.OrderItem;
import com.kshrd.product_orders_mgmt.model.entity.OrderItemId;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

}
