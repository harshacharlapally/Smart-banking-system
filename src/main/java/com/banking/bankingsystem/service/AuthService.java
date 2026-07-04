package com.banking.bankingsystem.service;

import com.banking.bankingsystem.dto.AuthResponse;
import com.banking.bankingsystem.dto.LoginRequest;
import com.banking.bankingsystem.dto.RegisterRequest;

import com.banking.bankingsystem.model.User;

import com.banking.bankingsystem.repository.UserRepository;

import com.banking.bankingsystem.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Register user
    public AuthResponse register(
            RegisterRequest request) {

        // Check duplicate email
        if(userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already exists");
        }

        // Create user object
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()))
                .role(User.Role.CUSTOMER)
                .build();

        // Save user
        userRepository.save(user);

        //Send welcome email
        emailService.sendWelcomeEmail(
                user.getEmail(),
                user.getName()
        );

        // Generate token
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name());

        // Return response
        return new AuthResponse(
                token,
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                "Registration successful"
        );
    }

    // Login user
    public AuthResponse login(
            LoginRequest request) {

        // Find user
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"));

        // Check password
        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password");
        }

        //Send login alert
        emailService.sendLoginAlert(
                user.getEmail(),
                user.getName()
        );

        // Generate token
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name());

        // Return response
        return new AuthResponse(
                token,
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                "Login successful"
        );
    }
}