package com.ecommerce.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.demo.dto.AuthResponse;
import com.ecommerce.demo.dto.LoginRequest;
import com.ecommerce.demo.dto.RegisterRequest;
import com.ecommerce.demo.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication APIs", description = "User authentication APIs")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @Operation(summary = "User login")
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        return authService.login(request);
    }

    @Operation(summary = "Get user profile")
    @GetMapping("/profile")
    public String profile() {

        return "Protected Profile API";
    }
}