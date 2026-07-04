package com.banking.bankingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.banking.bankingsystem.dto.AuthResponse;
import com.banking.bankingsystem.dto.LoginRequest;
import com.banking.bankingsystem.dto.RegisterRequest;
import com.banking.bankingsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication",
        description = "Register and Login APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Register API
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody
            RegisterRequest request) {

        AuthResponse response =
                authService.register(request);

        return ResponseEntity.ok(response);
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody
            LoginRequest request) {

        AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }
}