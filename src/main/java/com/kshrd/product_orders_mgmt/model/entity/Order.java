package com.kshrd.product_orders_mgmt.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.kshrd.product_orders_mgmt.model.dto.response.OrderResponse;
import com.kshrd.product_orders_mgmt.model.enumeration.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntityAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_amount", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public List<Product> getProducts() {
        if (this.orderItems == null || this.orderItems.isEmpty()) {
            return List.of();
        }
        return this.orderItems.stream()
                .map(OrderItem::getProduct)
                .toList();
    }

    @PrePersist
    private void prePersist() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }

        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }

    public OrderResponse toResponse() {
        return OrderResponse.builder()
                .orderId(orderId)
                .orderDate(orderDate)
                .totalAmount(totalAmount)
                .status(status)
                .customerResponse(customer.toResponse())
                .productResponses(getProducts().stream().map(Product::toResponse).toList()).build();
    }
}
