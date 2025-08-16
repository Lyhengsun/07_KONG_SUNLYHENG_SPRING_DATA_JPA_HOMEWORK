package com.kshrd.product_orders_mgmt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kshrd.product_orders_mgmt.model.dto.request.CustomerRequest;
import com.kshrd.product_orders_mgmt.model.dto.response.ApiResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.CustomerResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PagedResponse;
import com.kshrd.product_orders_mgmt.service.CustomerService;
import com.kshrd.product_orders_mgmt.utils.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{customer-id}")
    @Operation(summary = "Get customer by ID", description =  "Fetches detailed information about a single customer using the provided customer ID.")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable("customer-id") Long customerId) {
        CustomerResponse customer = customerService.findById(customerId);
        return ResponseUtils.createResponse("Fetch Customer with ID: " + customerId + " successfully", HttpStatus.FOUND,
                customer);
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Accepts a valid ```CustomerRequest``` object to create a new customer record. Returns the newly created customer's details.")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse customer = customerService.create(request);
        return ResponseUtils.createResponse("Customer created successfully", HttpStatus.CREATED, customer);
    }

    @PutMapping("/{customer-id}")
    @Operation(summary = "Update existing customer", description = "Updates the details of an existing customer based on the provided customer ID and request body.")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(@PathVariable("customer-id") Long customerId,
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse updatedCustomer = customerService.update(customerId, request);
        return ResponseUtils.createResponse("Updated customer with ID: " + customerId + " successfully", HttpStatus.OK,
                updatedCustomer);
    }

    @DeleteMapping("/{customer-id}")
    @Operation(summary = "Delete customer", description = "Removes a customer record from the system based on the provided customer ID.")
    public ResponseEntity<ApiResponse<CustomerResponse>> deleteCustomer(@PathVariable("customer-id") Long customerId) {
        CustomerResponse deletedCustomer = customerService.delete(customerId);
        return ResponseUtils.createResponse("Deleted customer with ID: " + customerId + " successfully", HttpStatus.OK,
                deletedCustomer);
    }

    @GetMapping
    @Operation(summary = "Get paginated list of customers", description = "Retrieves all customers in a paginated format. You can specify the page number, page size, sorting property, and sort direction.")
    public ResponseEntity<ApiResponse<PagedResponse<CustomerResponse>>> getAllCustomers(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        PagedResponse<CustomerResponse> pagedResponse = customerService.findAll(page, size);
        String message = "Fetch all customer successfully";
        if (pagedResponse.getItems().isEmpty()) {
            message = "No Record Found";
        }
        return ResponseUtils.createResponse(message, HttpStatus.OK, pagedResponse);
    }
}
