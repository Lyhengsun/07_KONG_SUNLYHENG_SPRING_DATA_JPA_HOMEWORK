package com.kshrd.product_orders_mgmt.model.entity;

import java.math.BigDecimal;

import com.kshrd.product_orders_mgmt.model.dto.response.ProductResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntityAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "unit_price", nullable = false, precision = 8, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "description")
    private String description;

    public ProductResponse toResponse() {
        return ProductResponse.builder()
                .id(this.productId)
                .name(this.name)
                .description(this.description)
                .unitPrice(this.unitPrice)
                .createdAt(getCreatedAt())
                .editedAt(getEditedAt())
                .build();
    }
}
