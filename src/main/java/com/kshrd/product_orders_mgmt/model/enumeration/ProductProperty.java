package com.kshrd.product_orders_mgmt.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductProperty {
    ID("productId"), NAME("name"), UNIT_PRICE("unitPrice");
    private final String value;
}
