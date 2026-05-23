package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.dto.ProductDto;
import com.ecommerce.orderservice.dto.UserDto;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.feign.ProductClient;
import com.ecommerce.orderservice.feign.UserClient;
import com.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserClient userClient;

    public OrderService(
            OrderRepository orderRepository,
            ProductClient productClient,
            UserClient userClient
    ) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.userClient = userClient;
    }

    public Order placeOrder(OrderRequest request) {

        // Validate User
        UserDto user = userClient.getUser(request.getUserId());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Validate Product
        ProductDto product =
                productClient.getProduct(request.getProductId());

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Check Quantity
        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Reduce Product Quantity
        productClient.reduceStock(
                request.getProductId(),
                request.getQuantity()
        );

        // Calculate Total Price
        Double totalPrice =
                product.getPrice() * request.getQuantity();

        // Create Order
        Order order = new Order();

        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        // Save Order
        return orderRepository.save(order);
    }
}