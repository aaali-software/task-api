package com.aziz.taskapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aziz.taskapi.dto.AuthRequest;
import com.aziz.taskapi.dto.AuthResponse;
import com.aziz.taskapi.dto.RefreshTokenRequest;
import com.aziz.taskapi.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Exposes authentication endpoints for registration and login.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user and returns a JWT for the created account.
     *
     * @param request credentials for the new user
     * @return authentication response containing a JWT
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody AuthRequest request) {
        log.info("Registration request received for username={}", request.getUsername());
        return authService.register(request);
    }

    /**
     * Authenticates an existing user and returns a JWT.
     *
     * @param request user credentials
     * @return authentication response containing a JWT
     */
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        log.info("Login request received for username={}", request.getUsername());
        return authService.login(request);
    }

    /**
     * Refreshes an access token using a valid refresh token.
     * 
     * @param request refresh token request containing the refresh token
     * @return authentication response containing a new access token and refresh
     *         token
     */
    @PostMapping("/refresh")
    public AuthResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Refresh token request received");
        return authService.refreshToken(request);
    }
}
