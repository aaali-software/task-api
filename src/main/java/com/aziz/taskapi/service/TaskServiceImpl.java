package com.aziz.taskapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.aziz.taskapi.dto.PagedResponse;
import com.aziz.taskapi.dto.TaskCreateRequest;
import com.aziz.taskapi.dto.TaskResponse;
import com.aziz.taskapi.dto.TaskStatusUpdateRequest;
import com.aziz.taskapi.dto.TaskUpdateRequest;
import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;
import com.aziz.taskapi.exception.ResourceNotFoundException;
import com.aziz.taskapi.repository.TaskRepository;

/**
 * Default {@link TaskService} implementation backed by {@link TaskRepository}.
 */

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public PagedResponse<TaskResponse> getAllTasks(
            TaskStatus status,
            TaskPriority priority,
            int page,
            int size,
            String sortBy,
            String direction) {
        log.debug("Fetching tasks with status={}, priority={}, page={}, size={}, sortBy={}, direction={}",
                status, priority, page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> taskPage;

        if (status != null && priority != null) {
            taskPage = taskRepository.findByStatusAndPriority(status, priority, pageable);
        } else if (status != null) {
            taskPage = taskRepository.findByStatus(status, pageable);
        } else if (priority != null) {
            taskPage = taskRepository.findByPriority(priority, pageable);
        } else {
            taskPage = taskRepository.findAll(pageable);
        }

        var content = taskPage.getContent()
                .stream()
                .map(this::mapToTaskResponse)
                .toList();

        return new PagedResponse<>(
                content,
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages(),
                taskPage.isLast());
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public TaskResponse getTaskById(Long id) {
        log.info("Fetching task by id={}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found for id={}", id);
                    return new ResourceNotFoundException("Task not found with id: " + id);
                });

        return mapToTaskResponse(task);
    }

    @Override
    public TaskResponse createTask(TaskCreateRequest request) {
        log.info("Creating task with title={}, priority={}", request.getTitle(), request.getPriority());
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
                .dueDate(request.getDueDate())
                .build();

        Task savedTask = taskRepository.save(task);
        log.info("Created task with id={}", savedTask.getId());
        return mapToTaskResponse(savedTask);
    }

    @Override
    @CacheEvict(value = "tasks", key = "#id")
    public TaskResponse updateTaskStatus(Long id, TaskStatusUpdateRequest request) {
        log.info("Updating task status for id={} to status={}", id, request.getStatus());

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found while updating status for id={}", id);
                    return new ResourceNotFoundException("Task not found with id: " + id);
                });

        task.setStatus(request.getStatus());
        Task updatedTask = taskRepository.save(task);

        return mapToTaskResponse(updatedTask);
    }

    @Override
    @CacheEvict(value = "tasks", key = "#id")
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        log.info("Updating task id={}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Task updatedTask = taskRepository.save(task);
        return mapToTaskResponse(updatedTask);
    }

    @Override
    @CacheEvict(value = "tasks", key = "#id")
    public void deleteTask(Long id) {
        log.info("Deleting task id={}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found while deleting id={}", id);
                    return new ResourceNotFoundException("Task not found with id: " + id);
                });

        taskRepository.delete(task);
        log.info("Task deleted successfully for id={}", id);
    }

    private TaskResponse mapToTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt());
    }
}
