package com.banking.bankingsystem.security;

import com.banking.bankingsystem.model.User;
import com.banking.bankingsystem.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get auth header
        String authHeader =
                request.getHeader("Authorization");

        // Check Bearer token
        if(authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer "
        String token = authHeader.substring(7);

        try {

            // Extract email
            String email =
                    jwtUtil.extractEmail(token);

            // If user not authenticated yet
            if(email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // Find user
                User user = userRepository
                        .findByEmail(email)
                        .orElse(null);

                // Validate token
                if(user != null &&
                        jwtUtil.validateToken(
                                token,
                                email)) {

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of(
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" + user.getRole().name()
                                            )
                                    )
                            );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(auth);
                }
            }

        } catch(Exception e) {

        }

        filterChain.doFilter(request, response);
    }
}