package com.aziz.taskapi.service;

import com.aziz.taskapi.entity.AppUser;
import com.aziz.taskapi.entity.RefreshToken;
import com.aziz.taskapi.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh.token.expiration}")
    private long refreshTokenDurationMs;

    public RefreshToken createOrReplaceRefreshToken(AppUser user) {
        refreshTokenRepository.findByUser(user)
                .ifPresent(existing -> {
                    log.info("Replacing existing refresh token for username={}", user.getUsername());
                    refreshTokenRepository.delete(existing);
                });

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plus(Duration.ofMillis(refreshTokenDurationMs)))
                .build();

        RefreshToken saved = refreshTokenRepository.save(refreshToken);
        log.info("Refresh token created for username={}", user.getUsername());
        return saved;
    }

    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Expired refresh token used for username={}", refreshToken.getUser().getUsername());
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token has expired");
        }

        return refreshToken;
    }

    public void deleteByUser(AppUser user) {
        refreshTokenRepository.deleteByUser(user);
    }
}