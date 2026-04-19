package com.aziz.taskapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aziz.taskapi.dto.TaskCreateRequest;
import com.aziz.taskapi.dto.TaskResponse;
import com.aziz.taskapi.dto.TaskStatusUpdateRequest;
import com.aziz.taskapi.dto.TaskUpdateRequest;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;
import com.aziz.taskapi.service.TaskService;
import com.aziz.taskapi.dto.PagedResponse;

import jakarta.validation.Valid;

/**
 * Exposes REST endpoints for managing tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Returns a paged list of tasks with optional status and priority filters.
     *
     * @param status optional status filter
     * @param priority optional priority filter
     * @param page zero-based page index
     * @param size page size
     * @param sort sort expression in the form {@code field[,direction]}
     * @return paged task response
     */
    @GetMapping
    public PagedResponse<TaskResponse> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        String direction = sortParams.length > 1 ? sortParams[1] : "asc";

        return taskService.getAllTasks(status, priority, page, size, sortBy, direction);
    }

    /**
     * Returns a task by its identifier.
     *
     * @param id task identifier
     * @return the matching task
     */
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    /**
     * Creates a new task.
     *
     * @param request task creation payload
     * @return the created task
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody TaskCreateRequest request) {
        return taskService.createTask(request);
    }

    /**
     * Updates only the status of an existing task.
     *
     * @param id task identifier
     * @param request status update payload
     * @return the updated task
     */
    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long id, @Valid 
                                @RequestBody TaskStatusUpdateRequest request) {
        return taskService.updateTaskStatus(id, request);
    }

    /**
     * Replaces the mutable fields of an existing task.
     *
     * @param id task identifier
     * @param request task update payload
     * @return the updated task
     */
    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id,
                            @Valid @RequestBody TaskUpdateRequest request) {
        return taskService.updateTask(id, request);
    }

    /**
     * Deletes a task by its identifier.
     *
     * @param id task identifier
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
