package com.kshrd.product_orders_mgmt.model.entity;

import com.kshrd.product_orders_mgmt.model.dto.response.CustomerResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer extends BaseEntityAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private CustomerAccount customerAccount;

    public CustomerResponse toResponse() {
        return CustomerResponse.builder()
                .customerId(this.customerId)
                .name(this.name)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .customerAccount(customerAccount != null ? customerAccount.toResponse() : null)
                .createdAt(getCreatedAt())
                .editedAt(getEditedAt())
                .build();
    }
}
