package com.aziz.taskapi.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.aziz.taskapi.config.RateLimitingFilter;

@SpringBootTest
@AutoConfigureMockMvc
class RateLimitingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    @BeforeEach
    void resetRateLimiter() {
        rateLimitingFilter.clearBuckets();
    }

    @Test
    @DisplayName("POST /api/auth/login should be rate limited after 5 attempts")
    void shouldRateLimitLoginEndpoint() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content("""
                            {
                              "username": "test",
                              "password": "wrong-password"
                            }
                            """))
                    .andExpect(status().isUnauthorized());
        }

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("""
                        {
                          "username": "test",
                          "password": "wrong-password"
                        }
                        """))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("GET /api/tasks should be rate limited after 60 requests")
    void shouldRateLimitTasksEndpoint() throws Exception {
        for (int i = 0; i < 60; i++) {
            mockMvc.perform(get("/api/tasks")
                    .with(user("aziz").roles("USER")))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/api/tasks")
                .with(user("aziz").roles("USER")))
                .andExpect(status().isTooManyRequests());
    }
}
