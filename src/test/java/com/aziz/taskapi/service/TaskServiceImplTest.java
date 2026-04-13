package com.aziz.taskapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.aziz.taskapi.dto.TaskStatusUpdateRequest;
import com.aziz.taskapi.dto.TaskUpdateRequest;
import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;
import com.aziz.taskapi.exception.ResourceNotFoundException;
import com.aziz.taskapi.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldReturnAllTasks() {
        Task task = Task.builder()
                .id(1L)
                .title("Test Task")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        Page<Task> page = new PageImpl<>(
                List.of(task),
                PageRequest.of(0, 10),
                1);

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = taskService.getAllTasks(null, null, 0, 10, "createdAt", "desc");

        assertEquals(1, result.getContent().size());
        assertEquals("Test Task", result.getContent().get(0).getTitle());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        verify(taskRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnAllTasksWithSorting() {
        Task task = Task.builder()
                .id(1L)
                .title("Test Task")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        Page<Task> page = new PageImpl<>(
                List.of(task),
                PageRequest.of(0, 5),
                1);

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = taskService.getAllTasks(null, null, 0, 5, "dueDate", "asc");

        assertEquals(1, result.getContent().size());
        assertEquals("Test Task", result.getContent().get(0).getTitle());

        verify(taskRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnTasksFilteredByStatus() {
        Task task = Task.builder()
                .id(1L)
                .title("Pending Task")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.MEDIUM)
                .build();

        Page<Task> page = new PageImpl<>(
                List.of(task),
                PageRequest.of(0, 10),
                1);

        when(taskRepository.findByStatus(eq(TaskStatus.PENDING), any(Pageable.class)))
                .thenReturn(page);

        var result = taskService.getAllTasks(TaskStatus.PENDING, null, 0, 10, "createdAt", "desc");

        assertEquals(1, result.getContent().size());
        assertEquals("Pending Task", result.getContent().get(0).getTitle());
        assertEquals(TaskStatus.PENDING, result.getContent().get(0).getStatus());

        verify(taskRepository, times(1)).findByStatus(eq(TaskStatus.PENDING), any(Pageable.class));
    }

    @Test
    void shouldReturnTasksFilteredByPriority() {
        Task task = Task.builder()
                .id(2L)
                .title("High Priority Task")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .build();

        Page<Task> page = new PageImpl<>(
                List.of(task),
                PageRequest.of(0, 10),
                1);

        when(taskRepository.findByPriority(eq(TaskPriority.HIGH), any(Pageable.class)))
                .thenReturn(page);

        var result = taskService.getAllTasks(null, TaskPriority.HIGH, 0, 10, "createdAt", "desc");

        assertEquals(1, result.getContent().size());
        assertEquals("High Priority Task", result.getContent().get(0).getTitle());
        assertEquals(TaskPriority.HIGH, result.getContent().get(0).getPriority());

        verify(taskRepository, times(1)).findByPriority(eq(TaskPriority.HIGH), any(Pageable.class));
    }

    @Test
    void shouldReturnTasksFilteredByStatusAndPriority() {
        Task task = Task.builder()
                .id(3L)
                .title("Filtered Task")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        Page<Task> page = new PageImpl<>(
                List.of(task),
                PageRequest.of(0, 10),
                1);

        when(taskRepository.findByStatusAndPriority(
                eq(TaskStatus.PENDING),
                eq(TaskPriority.HIGH),
                any(Pageable.class)))
                .thenReturn(page);

        var result = taskService.getAllTasks(TaskStatus.PENDING, TaskPriority.HIGH, 0, 10, "createdAt", "desc");

        assertEquals(1, result.getContent().size());
        assertEquals("Filtered Task", result.getContent().get(0).getTitle());
        assertEquals(TaskStatus.PENDING, result.getContent().get(0).getStatus());
        assertEquals(TaskPriority.HIGH, result.getContent().get(0).getPriority());

        verify(taskRepository, times(1)).findByStatusAndPriority(
                eq(TaskStatus.PENDING),
                eq(TaskPriority.HIGH),
                any(Pageable.class));
    }

    @Test
    void shouldReturnAllTasksWithAscendingSort() {
        Task task = Task.builder()
                .id(4L)
                .title("Ascending Sort Task")
                .status(TaskStatus.COMPLETED)
                .priority(TaskPriority.LOW)
                .build();

        Page<Task> page = new PageImpl<>(
                List.of(task),
                PageRequest.of(0, 5),
                1);

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = taskService.getAllTasks(null, null, 0, 5, "dueDate", "asc");

        assertEquals(1, result.getContent().size());
        assertEquals("Ascending Sort Task", result.getContent().get(0).getTitle());

        verify(taskRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnTaskById() {
        Task task = Task.builder()
                .id(1L)
                .title("Test Task")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));

        var result = taskService.getTaskById(1L);

        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(1L);
        });

        assertTrue(exception.getMessage().contains("Task not found"));

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateTask() {
        var request = new com.aziz.taskapi.dto.TaskCreateRequest();
        request.setTitle("New Task");
        request.setDescription("Test description");
        request.setPriority(TaskPriority.HIGH);

        Task savedTask = Task.builder()
                .id(1L)
                .title("New Task")
                .description("Test description")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        var result = taskService.createTask(request);

        assertEquals("New Task", result.getTitle());
        assertEquals(TaskPriority.HIGH, result.getPriority());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldUpdateTaskStatus() {
        Task existingTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        var request = new TaskStatusUpdateRequest();
        request.setStatus(TaskStatus.IN_PROGRESS);

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test description")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .build();

        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        var result = taskService.updateTaskStatus(1L, request);

        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldUpdateTask() {
        Task existingTask = Task.builder()
                .id(1L)
                .title("Old Title")
                .description("Old description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.LOW)
                .build();

        var request = new TaskUpdateRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated description");
        request.setStatus(TaskStatus.COMPLETED);
        request.setPriority(TaskPriority.HIGH);
        request.setDueDate(java.time.LocalDateTime.of(2026, 4, 15, 12, 0));

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Updated Title")
                .description("Updated description")
                .status(TaskStatus.COMPLETED)
                .priority(TaskPriority.HIGH)
                .dueDate(java.time.LocalDateTime.of(2026, 4, 15, 12, 0))
                .build();

        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        var result = taskService.updateTask(1L, request);

        assertEquals("Updated Title", result.getTitle());
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        assertEquals(TaskPriority.HIGH, result.getPriority());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldDeleteTask() {
        Task existingTask = Task.builder()
                .id(1L)
                .title("Task to delete")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.MEDIUM)
                .build();

        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTask));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(existingTask);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingStatusForMissingTask() {
        var request = new TaskStatusUpdateRequest();
        request.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTaskStatus(1L, request));

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingTask() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L));

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).delete(any(Task.class));
    }
}