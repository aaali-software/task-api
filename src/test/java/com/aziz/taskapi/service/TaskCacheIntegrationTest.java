package com.aziz.taskapi.service;

import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;
import com.aziz.taskapi.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskCacheIntegrationTest {

    @Autowired
    private TaskService taskService;

    @MockitoSpyBean
    private TaskRepository taskRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void shouldCacheTaskById() {
        Task task = Task.builder()
                .id(1L)
                .title("Cached Task")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.getTaskById(1L);
        taskService.getTaskById(1L);

        verify(taskRepository, times(1)).findById(1L);
    }
}