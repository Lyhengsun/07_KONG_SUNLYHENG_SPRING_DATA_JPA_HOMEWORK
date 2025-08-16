package com.kshrd.product_orders_mgmt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kshrd.product_orders_mgmt.model.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.customer.customerId = ?1")
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
}
