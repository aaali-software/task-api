package com.aziz.taskapi.controller;

import com.aziz.taskapi.service.TaskService;
import com.aziz.taskapi.config.RateLimitingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    @MockitoBean
    private TaskService taskService;

    @BeforeEach
    void resetRateLimiter() {
        rateLimitingFilter.clearBuckets();
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} without authentication should return 401")
    void shouldRejectDeleteWithoutAuthentication() throws Exception {
        mockMvc.perform(request(HttpMethod.DELETE, "/api/tasks/1"))
                .andExpect(status().isUnauthorized());

        verify(taskService, never()).deleteTask(1L);
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} as USER should return 403")
    void shouldRejectDeleteForUserRole() throws Exception {
        mockMvc.perform(request(HttpMethod.DELETE, "/api/tasks/1")
                        .with(user("aziz").roles("USER")))
                .andExpect(status().isForbidden());

        verify(taskService, never()).deleteTask(1L);
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} as ADMIN should return 204")
    void shouldAllowDeleteForAdminRole() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(request(HttpMethod.DELETE, "/api/tasks/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }
}
