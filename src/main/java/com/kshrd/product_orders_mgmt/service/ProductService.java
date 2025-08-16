package com.kshrd.product_orders_mgmt.service;

import com.kshrd.product_orders_mgmt.model.dto.request.ProductRequest;
import com.kshrd.product_orders_mgmt.model.dto.response.PagedResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse findById(Long productId);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long productId, ProductRequest request);

    ProductResponse delete(Long productId);

    PagedResponse<ProductResponse> getAllProducts(Integer page, Integer size);
    
}
