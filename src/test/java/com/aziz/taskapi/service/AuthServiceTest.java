package com.aziz.taskapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aziz.taskapi.dto.AuthRequest;
import com.aziz.taskapi.dto.AuthResponse;
import com.aziz.taskapi.entity.AppUser;
import com.aziz.taskapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        AuthRequest request = new AuthRequest();
        request.setUsername("aziz");
        request.setPassword("password123");

        when(userRepository.existsByUsername("aziz")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(jwtService.generateToken("aziz")).thenReturn("mock-jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals("mock-jwt-token", response.getToken());

        verify(userRepository, times(1)).existsByUsername("aziz");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(AppUser.class));
        verify(jwtService, times(1)).generateToken("aziz");
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        AuthRequest request = new AuthRequest();
        request.setUsername("aziz");
        request.setPassword("password123");

        when(userRepository.existsByUsername("aziz")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(request));

        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername("aziz");
        verify(userRepository, never()).save(any(AppUser.class));
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void shouldLoginUserSuccessfully() {
        AuthRequest request = new AuthRequest();
        request.setUsername("aziz");
        request.setPassword("password123");

        when(jwtService.generateToken("aziz")).thenReturn("mock-jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("mock-jwt-token", response.getToken());

        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("aziz");
    }
}