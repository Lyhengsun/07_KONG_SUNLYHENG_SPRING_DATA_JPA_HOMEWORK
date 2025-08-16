package com.kshrd.product_orders_mgmt.model.dto.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kshrd.product_orders_mgmt.model.entity.Customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "address is required")
    @Size(max = 100, message = "address cannot exceed 100 characters")
    private String address;

    @NotBlank(message = "phoneNumber is required")
    @Pattern(
            regexp = "^(0[1-9][0-9]{7}|\\+855[1-9][0-9]{7})$",
            message = "invalid cambodian phone number format"
    )
    private String phoneNumber;
    @JsonUnwrapped
    private CustomerAccountRequest customerAccount;

    public Customer toEntity() {
        return Customer.builder()
                .name(this.name)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}
