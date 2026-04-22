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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aziz.taskapi.dto.AuthRequest;
import com.aziz.taskapi.dto.AuthResponse;
import com.aziz.taskapi.dto.RefreshTokenRequest;
import com.aziz.taskapi.entity.AppUser;
import com.aziz.taskapi.entity.RefreshToken;
import com.aziz.taskapi.enums.Role;
import com.aziz.taskapi.exception.DuplicateUsernameException;
import com.aziz.taskapi.repository.UserRepository;

import java.time.LocalDateTime;

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

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        AuthRequest request = new AuthRequest();
        request.setUsername("aziz");
        request.setPassword("password123");

        when(userRepository.existsByUsername("aziz")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken("aziz")).thenReturn("mock-jwt-token");
        when(refreshTokenService.createOrReplaceRefreshToken(any(AppUser.class)))
                .thenReturn(buildRefreshToken("mock-refresh-token", buildUser("aziz")));

        AuthResponse response = authService.register(request);

        assertEquals("mock-jwt-token", response.getAccessToken());
        assertEquals("mock-refresh-token", response.getRefreshToken());

        verify(userRepository, times(1)).existsByUsername("aziz");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(jwtService, times(1)).generateToken("aziz");
        verify(refreshTokenService, times(1)).createOrReplaceRefreshToken(any(AppUser.class));
        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).save(userCaptor.capture());
        AppUser savedUser = userCaptor.getValue();
        assertEquals(Role.USER, savedUser.getRole());

    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        AuthRequest request = new AuthRequest();
        request.setUsername("aziz");
        request.setPassword("password123");

        when(userRepository.existsByUsername("aziz")).thenReturn(true);

        DuplicateUsernameException exception = assertThrows(
                DuplicateUsernameException.class,
                () -> authService.register(request));

        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername("aziz");
        verify(userRepository, never()).save(any(AppUser.class));
        verify(jwtService, never()).generateToken(anyString());
        verify(refreshTokenService, never()).createOrReplaceRefreshToken(any(AppUser.class));
    }

    @Test
    void shouldLoginUserSuccessfully() {
        AuthRequest request = new AuthRequest();
        request.setUsername("aziz");
        request.setPassword("password123");

        AppUser user = buildUser("aziz");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("aziz", "password123"));
        when(userRepository.findByUsername("aziz")).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken("aziz")).thenReturn("mock-jwt-token");
        when(refreshTokenService.createOrReplaceRefreshToken(user))
                .thenReturn(buildRefreshToken("mock-refresh-token", user));

        AuthResponse response = authService.login(request);

        assertEquals("mock-jwt-token", response.getAccessToken());
        assertEquals("mock-refresh-token", response.getRefreshToken());

        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("aziz");
        verify(jwtService, times(1)).generateToken("aziz");
        verify(refreshTokenService, times(1)).createOrReplaceRefreshToken(user);
    }

    @Test
    void shouldRefreshTokenSuccessfully() {
        AppUser user = buildUser("aziz");
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("old-refresh-token");

        when(refreshTokenService.verifyRefreshToken("old-refresh-token"))
                .thenReturn(buildRefreshToken("old-refresh-token", user));
        when(jwtService.generateToken("aziz")).thenReturn("new-access-token");
        when(refreshTokenService.createOrReplaceRefreshToken(user))
                .thenReturn(buildRefreshToken("new-refresh-token", user));

        AuthResponse response = authService.refreshToken(request);

        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());

        verify(refreshTokenService, times(1)).verifyRefreshToken("old-refresh-token");
        verify(jwtService, times(1)).generateToken("aziz");
        verify(refreshTokenService, times(1)).createOrReplaceRefreshToken(user);
    }

    private AppUser buildUser(String username) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword("encoded-password");
        user.setRole(Role.USER);
        return user;
    }

    private RefreshToken buildRefreshToken(String token, AppUser user) {
        return RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();
    }
}
