package com.ecommerce.orderservice.feign;

import com.ecommerce.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductDto getProduct(@PathVariable Long id);

    @PutMapping("/products/reduce-stock/{id}")
    void reduceStock(@PathVariable Long id,
                     @RequestParam Integer quantity);
}