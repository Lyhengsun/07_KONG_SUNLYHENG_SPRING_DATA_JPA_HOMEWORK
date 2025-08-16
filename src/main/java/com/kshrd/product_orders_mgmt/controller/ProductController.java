package com.kshrd.product_orders_mgmt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kshrd.product_orders_mgmt.model.dto.request.ProductRequest;
import com.kshrd.product_orders_mgmt.model.dto.response.ApiResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PagedResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.ProductResponse;
import com.kshrd.product_orders_mgmt.service.ProductService;
import com.kshrd.product_orders_mgmt.utils.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{product-id}")
    @Operation(summary = "Get product by ID", description = "Retrieves the details of a product using the specified product ID.")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable("product-id") Long productId) {
        ProductResponse product = productService.findById(productId);
        return ResponseUtils.createResponse("Fetch product with ID: " + productId + " successfully", HttpStatus.FOUND,
                product);
    }

    @PostMapping
    @Operation(summary = "Create a new product by ID", description = "Creates a new product record in the system using the provided ```ProductRequest``` payload. Returns the details of the newly created product.")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse product = productService.create(productRequest);
        return ResponseUtils.createResponse("Product created successfully", HttpStatus.CREATED, product);
    }

    @PutMapping("/{product-id}")
    @Operation(summary = "Update product by ID", description = "Updates the details of an existing product using the provided product ID and ```ProductRequest``` payload.")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable("product-id") Long productId,
            @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.update(productId, productRequest);
        return ResponseUtils.createResponse("Product with ID: " + productId + " updated successfully", HttpStatus.OK,
                updatedProduct);
    }

    @DeleteMapping("/{product-id}")
    @Operation(summary = "Delete a product by ID", description = "Deletes an existing product from the system using the specified product ID.")
    public ResponseEntity<ApiResponse<ProductResponse>> deleteProductById(@PathVariable("product-id") Long productId) {
        ProductResponse deletedProduct = productService.delete(productId);
        return ResponseUtils.createResponse("Product with ID: " + productId + " deleted successfully", HttpStatus.OK,
                deletedProduct);
    }

    @GetMapping
    @Operation(summary = "Get paginated list of products", description = "Retrieves a list of products in paginated format. You can specify the page number, page size, sorting property (from ```ProductProperty``` enum), and sort direction.")
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size) {

        PagedResponse<ProductResponse> pagedResponse = productService.getAllProducts(page, size);
        String message = "Fetched all products successfully";
        if (pagedResponse.getItems().isEmpty()) {
            message = "No Record Found";
        }
        return ResponseUtils.createResponse(message, HttpStatus.OK, pagedResponse);
    }

}
