package com.aziz.taskapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aziz.taskapi.dto.PagedResponse;
import com.aziz.taskapi.dto.TaskCreateRequest;
import com.aziz.taskapi.dto.TaskResponse;
import com.aziz.taskapi.dto.TaskStatusUpdateRequest;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;
import com.aziz.taskapi.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private TaskService taskService;

        private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        @Test
        @DisplayName("GET /api/tasks should return all tasks")
        void shouldReturnAllTasks() throws Exception {
                TaskResponse task = new TaskResponse(
                                1L,
                                "Test Task",
                                "Test description",
                                TaskStatus.PENDING,
                                TaskPriority.HIGH,
                                LocalDateTime.of(2026, 4, 20, 12, 0),
                                LocalDateTime.of(2026, 4, 10, 9, 0),
                                null);

                PagedResponse<TaskResponse> response = new PagedResponse<>(
                                List.of(task),
                                0,
                                10,
                                1,
                                1,
                                true);

                when(taskService.getAllTasks(null, null, 0, 10, "createdAt", "desc"))
                                .thenReturn(response);

                mockMvc.perform(get("/api/tasks"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.content[0].id").value(1))
                                .andExpect(jsonPath("$.content[0].title").value("Test Task"))
                                .andExpect(jsonPath("$.content[0].status").value("PENDING"))
                                .andExpect(jsonPath("$.content[0].priority").value("HIGH"))
                                .andExpect(jsonPath("$.page").value(0))
                                .andExpect(jsonPath("$.size").value(10))
                                .andExpect(jsonPath("$.totalElements").value(1))
                                .andExpect(jsonPath("$.totalPages").value(1))
                                .andExpect(jsonPath("$.last").value(true));

                verify(taskService).getAllTasks(null, null, 0, 10, "createdAt", "desc");
        }

        @Test
        @DisplayName("GET /api/tasks/{id} should return task by id")
        void shouldReturnTaskById() throws Exception {
                TaskResponse task = new TaskResponse(
                                1L,
                                "Test Task",
                                "Test description",
                                TaskStatus.IN_PROGRESS,
                                TaskPriority.MEDIUM,
                                LocalDateTime.of(2026, 4, 21, 15, 0),
                                LocalDateTime.of(2026, 4, 10, 9, 0),
                                LocalDateTime.of(2026, 4, 11, 10, 0));

                when(taskService.getTaskById(1L)).thenReturn(task);

                mockMvc.perform(get("/api/tasks/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Test Task"))
                                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

                verify(taskService).getTaskById(1L);
        }

        @Test
        @DisplayName("POST /api/tasks should create a task and return 201")
        void shouldCreateTask() throws Exception {
                TaskCreateRequest request = new TaskCreateRequest();
                request.setTitle("New Task");
                request.setDescription("Create controller test");
                request.setPriority(TaskPriority.HIGH);
                request.setDueDate(LocalDateTime.of(2026, 4, 25, 18, 0));

                TaskResponse response = new TaskResponse(
                                1L,
                                "New Task",
                                "Create controller test",
                                TaskStatus.PENDING,
                                TaskPriority.HIGH,
                                LocalDateTime.of(2026, 4, 25, 18, 0),
                                LocalDateTime.of(2026, 4, 10, 10, 0),
                                null);

                when(taskService.createTask(any(TaskCreateRequest.class))).thenReturn(response);

                mockMvc.perform(post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("New Task"))
                                .andExpect(jsonPath("$.status").value("PENDING"))
                                .andExpect(jsonPath("$.priority").value("HIGH"));

                verify(taskService).createTask(any(TaskCreateRequest.class));
        }

        @Test
        @DisplayName("POST /api/tasks with invalid payload should return 400")
        void shouldReturnBadRequestForInvalidCreateTaskRequest() throws Exception {
                TaskCreateRequest request = new TaskCreateRequest();
                request.setTitle("");
                request.setDescription("Invalid task");
                request.setPriority(null);

                mockMvc.perform(post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.message").value("Validation failed"))
                                .andExpect(jsonPath("$.validationErrors.title").exists())
                                .andExpect(jsonPath("$.validationErrors.priority").exists());
        }

        @Test
        void shouldReturn404WhenTaskNotFound() throws Exception {
                when(taskService.getTaskById(1L))
                                .thenThrow(new com.aziz.taskapi.exception.ResourceNotFoundException("Task not found"));

                mockMvc.perform(get("/api/tasks/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Task not found"));
        }

        @Test
        void shouldUpdateTaskStatus() throws Exception {
                var request = new TaskStatusUpdateRequest();
                request.setStatus(TaskStatus.IN_PROGRESS);

                TaskResponse response = new TaskResponse(
                                1L,
                                "Task",
                                "desc",
                                TaskStatus.IN_PROGRESS,
                                TaskPriority.HIGH,
                                null,
                                null,
                                null);

                when(taskService.updateTaskStatus(eq(1L), any(TaskStatusUpdateRequest.class)))
                                .thenReturn(response);

                mockMvc.perform(patch("/api/tasks/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
        }

        @Test
        void shouldUpdateTask() throws Exception {
                var request = new com.aziz.taskapi.dto.TaskUpdateRequest();
                request.setTitle("Updated");
                request.setDescription("Updated desc");
                request.setStatus(TaskStatus.COMPLETED);
                request.setPriority(TaskPriority.HIGH);

                TaskResponse response = new TaskResponse(
                                1L,
                                "Updated",
                                "Updated desc",
                                TaskStatus.COMPLETED,
                                TaskPriority.HIGH,
                                null,
                                null,
                                null);

                when(taskService.updateTask(eq(1L), any())).thenReturn(response);

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated"))
                                .andExpect(jsonPath("$.status").value("COMPLETED"));
        }

        @Test
        void shouldDeleteTask() throws Exception {
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/tasks/1"))
                                .andExpect(status().isNoContent());

                verify(taskService).deleteTask(1L);
        }

        @Test
        void shouldFilterTasksByStatus() throws Exception {
                TaskResponse task = new TaskResponse(
                                1L,
                                "Task",
                                "desc",
                                TaskStatus.PENDING,
                                TaskPriority.HIGH,
                                null,
                                null,
                                null);

                PagedResponse<TaskResponse> response = new PagedResponse<>(
                                List.of(task),
                                0,
                                10,
                                1,
                                1,
                                true);

                when(taskService.getAllTasks(TaskStatus.PENDING, null, 0, 10, "createdAt", "desc"))
                                .thenReturn(response);

                mockMvc.perform(get("/api/tasks?status=PENDING"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].status").value("PENDING"))
                                .andExpect(jsonPath("$.page").value(0))
                                .andExpect(jsonPath("$.size").value(10));

                verify(taskService).getAllTasks(TaskStatus.PENDING, null, 0, 10, "createdAt", "desc");
        }

        @Test
        void shouldReturnTasksWithCustomSorting() throws Exception {
                PagedResponse<TaskResponse> response = new PagedResponse<>(
                                List.of(),
                                0,
                                5,
                                0,
                                0,
                                true);

                when(taskService.getAllTasks(null, null, 0, 5, "dueDate", "asc"))
                                .thenReturn(response);

                mockMvc.perform(get("/api/tasks?page=0&size=5&sort=dueDate,asc"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.page").value(0))
                                .andExpect(jsonPath("$.size").value(5));

                verify(taskService).getAllTasks(null, null, 0, 5, "dueDate", "asc");
        }
}