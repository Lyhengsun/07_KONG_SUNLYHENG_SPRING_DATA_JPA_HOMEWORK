package com.kshrd.product_orders_mgmt.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderProperty {
    ID("orderId"), ORDER_DATE("orderDate"), TOTAL_AMOUNT("totalAmount");
    private final String value;
}
