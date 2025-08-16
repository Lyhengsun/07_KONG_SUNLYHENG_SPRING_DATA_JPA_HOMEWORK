package com.kshrd.product_orders_mgmt.model.dto.request;

import java.math.BigDecimal;

import com.kshrd.product_orders_mgmt.model.entity.Product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "unitPrice is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "unitPrice must be greater than 0")
    @Digits(integer = 6, fraction = 2, message = "unitPrice must be a valid 6 integer number with up to 2 decimal places")
    private BigDecimal unitPrice;

    private String description;

    public Product toEntity() {
        return Product.builder()
                .name(this.name)
                .description(this.description)
                .unitPrice(this.unitPrice)
                .build();
    }
}
