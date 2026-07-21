package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.request.LoginRequest;
import com.ecommerce.backend.dto.request.RegisterRequest;
import com.ecommerce.backend.dto.response.AuthResponse;

public interface AuthService {

    /**
     * Registers a new customer account.
     *
     * @param request registration details
     * @return authentication response containing a JWT and user information
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates an existing user.
     *
     * @param request login credentials
     * @return authentication response containing a JWT and user information
     */
    AuthResponse login(LoginRequest request);
}