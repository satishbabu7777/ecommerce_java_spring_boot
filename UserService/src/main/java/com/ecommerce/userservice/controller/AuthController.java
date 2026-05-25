package com.ecommerce.userservice.controller;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.userservice.dto.AuthResponse;
import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.RegisterRequest;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(
    name = "Authentication APIs",
    description = "User authentication APIs"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public User register(
            @RequestBody RegisterRequest request
    ) {

        return authService.register(request);
    }

    @Operation(summary = "User login")
    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }

    @Operation(summary = "Protected profile API")
    @GetMapping("/profile")
    public String profile() {

        return "Protected Profile API";
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable Long id
    ) {
        return authService.getUserById(id);
    }
}