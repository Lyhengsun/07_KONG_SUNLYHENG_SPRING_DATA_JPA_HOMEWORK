package com.kshrd.product_orders_mgmt.model.dto.request;

import com.kshrd.product_orders_mgmt.model.entity.CustomerAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class CustomerAccountRequest {
    @NotBlank(message = "username is required")
    @Size(min = 4, max = 100, message = "username must be between 4-100 characters")
    private String username;

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 255, message = "password must be between 6-255 characters")
    private String password;

    public CustomerAccount toEntity() {
        return CustomerAccount.builder()
                .username(this.username)
                .password(this.password)
                .build();
    }
}