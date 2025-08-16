package com.kshrd.product_orders_mgmt.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CustomerProperty {
    ID("customerId"), NAME("name");
    private final String value;
}
