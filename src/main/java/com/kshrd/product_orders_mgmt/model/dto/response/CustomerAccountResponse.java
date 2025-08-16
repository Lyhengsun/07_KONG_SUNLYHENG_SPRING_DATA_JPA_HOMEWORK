package com.kshrd.product_orders_mgmt.model.dto.response;

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
public class CustomerAccountResponse {
    private String username;
    private String password;
    private Boolean isActive;
}
