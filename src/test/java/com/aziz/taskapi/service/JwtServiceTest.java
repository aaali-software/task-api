package com.aziz.taskapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                "bXktc3VwZXItc2VjcmV0LWtleS1mb3Itand0LWRldi1vbmx5LTEyMzQ1Njc4OTA="
        );

        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken("aziz");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsernameFromToken() {
        String token = jwtService.generateToken("aziz");

        String username = jwtService.extractUsername(token);

        assertEquals("aziz", username);
    }

    @Test
    void shouldValidateTokenForCorrectUsername() {
        String token = jwtService.generateToken("aziz");

        boolean valid = jwtService.isTokenValid(token, "aziz");

        assertTrue(valid);
    }

    @Test
    void shouldRejectTokenForWrongUsername() {
        String token = jwtService.generateToken("aziz");

        boolean valid = jwtService.isTokenValid(token, "not-aziz");

        assertFalse(valid);
    }

    @Test
    void shouldRejectExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1000L);

        String token = jwtService.generateToken("aziz");

        boolean valid = jwtService.isTokenValid(token, "aziz");

        assertFalse(valid);
    }
}