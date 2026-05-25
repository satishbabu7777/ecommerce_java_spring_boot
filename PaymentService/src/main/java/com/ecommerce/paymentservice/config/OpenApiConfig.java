package com.ecommerce.paymentservice.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Payment Service API",
                version = "1.0",
                description = "APIs for Payment Service"
        )
)
public class OpenApiConfig {
    
}
