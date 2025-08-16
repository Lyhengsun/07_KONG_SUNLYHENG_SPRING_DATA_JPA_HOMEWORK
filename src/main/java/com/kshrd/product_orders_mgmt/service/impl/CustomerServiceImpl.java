package com.kshrd.product_orders_mgmt.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kshrd.product_orders_mgmt.exception.NotFoundException;
import com.kshrd.product_orders_mgmt.model.dto.request.CustomerAccountRequest;
import com.kshrd.product_orders_mgmt.model.dto.request.CustomerRequest;
import com.kshrd.product_orders_mgmt.model.dto.response.CustomerResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PagedResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PaginationInfo;
import com.kshrd.product_orders_mgmt.model.entity.Customer;
import com.kshrd.product_orders_mgmt.model.entity.CustomerAccount;
import com.kshrd.product_orders_mgmt.repository.CustomerRepository;
import com.kshrd.product_orders_mgmt.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public Customer findEntityById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> {
            throw new NotFoundException("Customer with ID: " + customerId + " doesn't exist");
        });
    }

    @Override
    public CustomerResponse findById(Long customerId) {
        return findEntityById(customerId).toResponse();
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        Customer newCustomer = request.toEntity();
        CustomerAccount newCustomerAccount = request.getCustomerAccount().toEntity();
        newCustomerAccount.setCustomer(newCustomer);
        newCustomer.setCustomerAccount(newCustomerAccount);
        return customerRepository.save(newCustomer).toResponse();
    }

    @Override
    public CustomerResponse update(Long customerId, CustomerRequest request) {
        Customer existingCustomer = findEntityById(customerId);
        CustomerAccount oldAccount = existingCustomer.getCustomerAccount();
        existingCustomer.setName(request.getName());
        existingCustomer.setAddress(request.getAddress());
        existingCustomer.setPhoneNumber(request.getPhoneNumber());

        CustomerAccountRequest newAccountInfo = request.getCustomerAccount();
        oldAccount.setUsername(newAccountInfo.getUsername());
        oldAccount.setPassword(newAccountInfo.getPassword());

        return customerRepository.save(existingCustomer).toResponse();
    }

    @Override
    public CustomerResponse delete(Long customerId) {
        Customer existingCustomer = findEntityById(customerId);
        customerRepository.delete(existingCustomer);
        return existingCustomer.toResponse();
    }

    @Override
    public PagedResponse<CustomerResponse> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return PagedResponse.<CustomerResponse>builder()
                .items(customerPage.getContent().stream().map(Customer::toResponse).toList())
                .pagination(new PaginationInfo(customerPage)).build();
    }
}
