package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.dto.ProductDto;
import com.ecommerce.orderservice.dto.UserDto;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.feign.PaymentClient;
import com.ecommerce.orderservice.feign.ProductClient;
import com.ecommerce.orderservice.feign.UserClient;
import com.ecommerce.orderservice.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final PaymentClient paymentClient;

    public OrderService(
            OrderRepository orderRepository,
            ProductClient productClient,
            UserClient userClient,
            PaymentClient paymentClient
    ) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.userClient = userClient;
        this.paymentClient = paymentClient;
    }

    @CircuitBreaker(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public Order placeOrder(OrderRequest request) {

        // Validate User
        UserDto user =
                userClient.getUser(request.getUserId());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Validate Product
        ProductDto product =
                productClient.getProduct(request.getProductId());

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Check Stock
        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Reduce Stock
        productClient.reduceStock(
                request.getProductId(),
                request.getQuantity()
        );

        // Create Order
        Order order = new Order();

        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());

        order.setTotalPrice(
                product.getPrice() * request.getQuantity()
        );

        order.setStatus("PLACED");

        order.setCreatedAt(LocalDateTime.now());

        // Save Order
        Order savedOrder =
                orderRepository.save(order);

        // CALL PAYMENT SERVICE
        String paymentResponse =
                paymentClient.createOrder(
                        savedOrder.getTotalPrice()
                );

        System.out.println(paymentResponse);

        return savedOrder;
    }

    // FALLBACK METHOD
    public Order paymentFallback(
            OrderRequest request,
            Exception ex
    ) {

        System.out.println(
                "Circuit Breaker Activated : "
                        + ex.getMessage()
        );

        Order failedOrder = new Order();

        failedOrder.setUserId(request.getUserId());

        failedOrder.setProductId(
                request.getProductId()
        );

        failedOrder.setQuantity(
                request.getQuantity()
        );

        failedOrder.setStatus(
                "PAYMENT_PENDING"
        );

        failedOrder.setCreatedAt(
                LocalDateTime.now()
        );

        return failedOrder;
    }
}