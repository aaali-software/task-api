package com.aziz.taskapi.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aziz.taskapi.dto.AuthRequest;
import com.aziz.taskapi.dto.AuthResponse;
import com.aziz.taskapi.dto.RefreshTokenRequest;
import com.aziz.taskapi.entity.AppUser;
import com.aziz.taskapi.enums.Role;
import com.aziz.taskapi.exception.DuplicateUsernameException;
import com.aziz.taskapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles user registration and login workflows.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    /**
     * Creates a user account and returns an authentication token.
     *
     * @param request requested username and password
     * @return JWT response for the created user
     */
    public AuthResponse register(AuthRequest request) {
        log.info("Attempting to register username={}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration failed: username already exists: {}", request.getUsername());
            throw new DuplicateUsernameException("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        AppUser savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        String accessToken = jwtService.generateToken(savedUser.getUsername());
        String refreshToken = refreshTokenService.createOrReplaceRefreshToken(savedUser).getToken();

        return new AuthResponse(accessToken, refreshToken);
    }

    /**
     * Authenticates a user and returns an authentication token.
     *
     * @param request username and password
     * @return JWT response for the authenticated user
     */
    public AuthResponse login(AuthRequest request) {
        log.info("Attempting login for username={}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("Login successful for username={}", request.getUsername());

        String accessToken = jwtService.generateToken(user.getUsername());
        String refreshToken = refreshTokenService.createOrReplaceRefreshToken(user).getToken();

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        var storedRefreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        AppUser user = storedRefreshToken.getUser();

        String newAccessToken = jwtService.generateToken(user.getUsername());
        String newRefreshToken = refreshTokenService.createOrReplaceRefreshToken(user).getToken();

        log.info("Refresh token flow successful for username={}", user.getUsername());

        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}
