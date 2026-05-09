package com.aziz.taskapi.service;

import com.aziz.taskapi.entity.AppUser;
import com.aziz.taskapi.entity.RefreshToken;
import com.aziz.taskapi.enums.Role;
import com.aziz.taskapi.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setId(1L);
        user.setUsername("aziz");
        user.setPassword("encoded-password");
        user.setRole(Role.USER);

        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", 604800000L);
    }

    @Test
    void shouldCreateRefreshTokenWhenNoneExists() {
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken result = refreshTokenService.createOrReplaceRefreshToken(user);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(user, result.getUser());
        assertNotNull(result.getExpiryDate());

        verify(refreshTokenRepository).findByUser(user);
        verify(refreshTokenRepository).save(captor.capture());

        RefreshToken saved = captor.getValue();
        assertEquals(user, saved.getUser());
        assertNotNull(saved.getToken());
        assertTrue(saved.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    @Test
    void shouldReplaceExistingRefreshToken() {
        RefreshToken existing = RefreshToken.builder()
                .id(10L)
                .token("old-token")
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();

        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(existing));
        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken result = refreshTokenService.createOrReplaceRefreshToken(user);

        assertNotNull(result);
        assertNotEquals("old-token", result.getToken());

        verify(refreshTokenRepository).findByUser(user);
        verify(refreshTokenRepository).delete(existing);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void shouldVerifyValidRefreshToken() {
        RefreshToken token = RefreshToken.builder()
                .id(1L)
                .token("valid-token")
                .user(user)
                .expiryDate(LocalDateTime.now().plus(Duration.ofDays(1)))
                .build();

        when(refreshTokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

        RefreshToken result = refreshTokenService.verifyRefreshToken("valid-token");

        assertEquals(token, result);
        verify(refreshTokenRepository).findByToken("valid-token");
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void shouldThrowWhenRefreshTokenIsInvalid() {
        when(refreshTokenRepository.findByToken("bad-token")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> refreshTokenService.verifyRefreshToken("bad-token")
        );

        assertEquals("Invalid refresh token", exception.getMessage());

        verify(refreshTokenRepository).findByToken("bad-token");
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void shouldThrowWhenRefreshTokenIsExpiredAndDeleteIt() {
        RefreshToken expired = RefreshToken.builder()
                .id(1L)
                .token("expired-token")
                .user(user)
                .expiryDate(LocalDateTime.now().minusMinutes(1))
                .build();

        when(refreshTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(expired));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> refreshTokenService.verifyRefreshToken("expired-token")
        );

        assertEquals("Refresh token has expired", exception.getMessage());

        verify(refreshTokenRepository).findByToken("expired-token");
        verify(refreshTokenRepository).delete(expired);
    }

    @Test
    void shouldDeleteRefreshTokenByUser() {
        refreshTokenService.deleteByUser(user);

        verify(refreshTokenRepository).deleteByUser(user);
    }
}