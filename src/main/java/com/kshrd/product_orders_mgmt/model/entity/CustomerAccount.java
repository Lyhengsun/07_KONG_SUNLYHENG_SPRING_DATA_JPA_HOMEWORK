package com.kshrd.product_orders_mgmt.model.entity;

import com.kshrd.product_orders_mgmt.model.dto.response.CustomerAccountResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAccount extends BaseEntityAudit {
    @Id
    @Column(name = "customer_id")
    private Long customerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @PrePersist
    private void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }

    public CustomerAccountResponse toResponse() {
        return CustomerAccountResponse.builder()
                .username(username)
                .password(password)
                .isActive(isActive)
                .build();
    }
}
