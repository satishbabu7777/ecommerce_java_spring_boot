package com.ecommerce.paymentservice.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;

    public PaymentService() throws Exception {

        this.razorpayClient =
                new RazorpayClient(
                        "rzp_test_Ssn4eIKgveB1vK",
                        "eOn2BV0eYyuPinL3xsdcK570"
                );
    }

    @CircuitBreaker(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public String createOrder(Double amount) throws Exception {

        JSONObject options = new JSONObject();

        options.put("amount", amount * 100);
        options.put("currency", "INR");
        options.put("receipt", "txn_123456");

        Order order = razorpayClient.orders.create(options);

        return order.toString();
    }

    // =========================
    // FALLBACK METHOD
    // =========================

    public String paymentFallback(Double amount, Exception ex) {

        return """
                {
                    "status":"FAILED",
                    "message":"Payment service temporarily unavailable",
                    "amount": %s
                }
                """.formatted(amount);
    }
}