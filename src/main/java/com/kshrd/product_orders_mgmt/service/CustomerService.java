package com.kshrd.product_orders_mgmt.service;

import com.kshrd.product_orders_mgmt.model.dto.request.CustomerRequest;
import com.kshrd.product_orders_mgmt.model.dto.response.CustomerResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PagedResponse;

public interface CustomerService {

    CustomerResponse findById(Long customerId);

    CustomerResponse create(CustomerRequest request);

    CustomerResponse update(Long customerId, CustomerRequest request);

    CustomerResponse delete(Long customerId);

    PagedResponse<CustomerResponse> findAll(Integer page, Integer size);
    
}
