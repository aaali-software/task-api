package com.aziz.taskapi.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aziz.taskapi.dto.AuthRequest;
import com.aziz.taskapi.dto.AuthResponse;
import com.aziz.taskapi.entity.AppUser;
import com.aziz.taskapi.exception.DuplicateUsernameException;
import com.aziz.taskapi.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles user registration and login workflows.
 */
@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
            this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Creates a user account and returns an authentication token.
     *
     * @param request requested username and password
     * @return JWT response for the created user
     */
    public AuthResponse register(AuthRequest request) {
        log.debug("Attempting registration for username={}", request.getUsername());
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration rejected because username already exists: {}", request.getUsername());
            throw new DuplicateUsernameException("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        log.info("User registered successfully for username={}", user.getUsername());

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    /**
     * Authenticates a user and returns an authentication token.
     *
     * @param request username and password
     * @return JWT response for the authenticated user
     */
    public AuthResponse login(AuthRequest request) {
        log.debug("Authenticating user with username={}", request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        log.info("User authenticated successfully for username={}", request.getUsername());

        String token = jwtService.generateToken(request.getUsername());
        return new AuthResponse(token);
    }
}
