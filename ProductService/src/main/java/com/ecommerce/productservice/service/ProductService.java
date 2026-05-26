package com.ecommerce.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.repository.ProductRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "addProductFallback")
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "updateProductFallback")
    public Product updateProduct(Long id, Product product) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());

        return productRepository.save(existing);
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "getAllProductsFallback")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "deleteProductFallback")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // =========================
    // FALLBACK METHODS
    // =========================

    public Product addProductFallback(Product product, Exception ex) {

        Product fallback = new Product();

        fallback.setName("Fallback Product");
        fallback.setDescription("Service temporarily unavailable");
        fallback.setPrice(0.0);
        fallback.setQuantity(0);

        return fallback;
    }

    public Product updateProductFallback(Long id, Product product, Exception ex) {

        Product fallback = new Product();

        fallback.setId(id);
        fallback.setName("Update Failed");
        fallback.setDescription("Fallback response");
        fallback.setPrice(0.0);
        fallback.setQuantity(0);

        return fallback;
    }

    public List<Product> getAllProductsFallback(Exception ex) {
        return List.of();
    }

    public void deleteProductFallback(Long id, Exception ex) {
        System.out.println("Delete operation failed for product id: " + id);
    }
}