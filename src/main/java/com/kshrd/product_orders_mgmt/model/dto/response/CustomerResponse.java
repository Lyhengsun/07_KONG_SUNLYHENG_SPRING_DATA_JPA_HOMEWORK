package com.kshrd.product_orders_mgmt.model.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private Long customerId;
    private String name;
    private String address;
    private String phoneNumber;
    @JsonUnwrapped
    private CustomerAccountResponse customerAccount;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
