package com.aziz.taskapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aziz.taskapi.dto.AuthRequest;
import com.aziz.taskapi.dto.AuthResponse;
import com.aziz.taskapi.dto.RefreshTokenRequest;
import com.aziz.taskapi.exception.DuplicateUsernameException;
import com.aziz.taskapi.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @Test
    @DisplayName("POST /api/auth/register should return token and 201")
    void shouldRegisterUser() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("aziz");
        request.setPassword("password123");

        AuthResponse response = new AuthResponse("mock-access-token", "mock-refresh-token");

        when(authService.register(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value("mock-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("mock-refresh-token"));
    }

    @Test
    @DisplayName("POST /api/auth/register with duplicate username should return 409")
    void shouldReturnConflictWhenRegisteringDuplicateUsername() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("aziz");
        request.setPassword("password123");

        when(authService.register(any(AuthRequest.class)))
                .thenThrow(new DuplicateUsernameException("Username already exists"));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    @DisplayName("POST /api/auth/login should return token and 200")
    void shouldLoginUser() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("aziz");
        request.setPassword("password123");

        AuthResponse response = new AuthResponse("mock-access-token", "mock-refresh-token");

        when(authService.login(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value("mock-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("mock-refresh-token"));
    }

    @Test
    @DisplayName("POST /api/auth/refresh should return new tokens and 200")
    void shouldRefreshTokens() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("existing-refresh-token");

        AuthResponse response = new AuthResponse("new-access-token", "new-refresh-token");

        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }

    @Test
    @DisplayName("POST /api/auth/register with invalid payload should return 400")
    void shouldRejectInvalidRegisterRequest() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("");
        request.setPassword("");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login with invalid payload should return 400")
    void shouldRejectInvalidLoginRequest() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("");
        request.setPassword("");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/refresh with invalid payload should return 400")
    void shouldRejectInvalidRefreshRequest() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("");

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
