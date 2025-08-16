package com.kshrd.product_orders_mgmt.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kshrd.product_orders_mgmt.exception.NotFoundException;
import com.kshrd.product_orders_mgmt.model.dto.request.ProductRequest;
import com.kshrd.product_orders_mgmt.model.dto.response.PagedResponse;
import com.kshrd.product_orders_mgmt.model.dto.response.PaginationInfo;
import com.kshrd.product_orders_mgmt.model.dto.response.ProductResponse;
import com.kshrd.product_orders_mgmt.model.entity.Product;
import com.kshrd.product_orders_mgmt.repository.ProductRepository;
import com.kshrd.product_orders_mgmt.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public Product findEntityById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product with ID: " + productId + " doesn't exist"));
    }

    @Override
    public ProductResponse findById(Long productId) {
        Product product = findEntityById(productId);
        return product.toResponse();
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = request.toEntity();
        return productRepository.save(product).toResponse();
    }

    @Override
    public ProductResponse update(Long productId, ProductRequest request) {
        Product existingProduct = findEntityById(productId);
        existingProduct.setName(request.getName());
        existingProduct.setUnitPrice(request.getUnitPrice());
        existingProduct.setDescription(request.getDescription());
        return productRepository.save(existingProduct).toResponse();
    }

    @Override
    public ProductResponse delete(Long productId) {
        Product product = findEntityById(productId);
        productRepository.delete(product);
        return product.toResponse();
    }

    @Override
    public PagedResponse<ProductResponse> getAllProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        return PagedResponse.<ProductResponse>builder()
                .items(productPage.getContent().stream().map(Product::toResponse).toList())
                .pagination(new PaginationInfo(productPage))
                .build();
    }

}
