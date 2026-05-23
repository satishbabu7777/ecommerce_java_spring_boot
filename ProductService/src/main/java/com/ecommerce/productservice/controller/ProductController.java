package com.ecommerce.productservice.controller;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return repository.save(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
    }

    @PutMapping("/reduce-stock/{id}")
    public String reduceStock(
            @PathVariable Long id,
            @RequestParam Integer quantity
    ) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        if(product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setQuantity(product.getQuantity() - quantity);

        repository.save(product);

        return "Stock Updated";
    }
}