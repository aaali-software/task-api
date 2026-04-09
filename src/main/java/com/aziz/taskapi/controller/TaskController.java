package com.aziz.taskapi.controller;

import java.util.List;

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
import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;
import com.aziz.taskapi.service.TaskService;

import jakarta.validation.Valid;

/**
 * Controller for handling Task-related requests.
 * This class provides REST endpoints for retrieving all tasks and getting a task by its ID.
 * It uses the TaskService to delegate the business logic and interact with the TaskRepository.
 * I created a TaskController class annotated with @RestController and @RequestMapping to define the base URL for task-related endpoints.
 * The controller has two endpoints: one for fetching all tasks (GET /api/tasks) and another for retrieving a specific task by its ID (GET /api/tasks/{id}).
 * Additionally, I added a POST endpoint to create a new task, which accepts a TaskCreateRequest object in the request body and returns the created Task with a 201 Created status.
 * I also added a PATCH endpoint to update the status of an existing task, which accepts a TaskStatusUpdateRequest object in the request body and returns the updated Task.
 * The controller methods call the corresponding service methods to perform the necessary operations and return the results as JSON responses.
 * I also added a PUT endpoint to update other fields of a Task (e.g., title, description, priority, due date) using a TaskUpdateRequest object, which can be implemented in the future if needed.
 * Finally, I added a DELETE endpoint to delete a Task by its ID, which can be implemented in the future to provide full CRUD functionality for Task entities.
 * This design follows the typical Spring Boot architecture, separating concerns between the controller (handling HTTP requests), service (business logic), and repository (data access).
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getAllTasks(@RequestParam(required = false) TaskStatus status,
                                @RequestParam(required = false) TaskPriority priority) {
        return taskService.getAllTasks(status, priority);
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody TaskCreateRequest request) {
        return taskService.createTask(request);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long id, @Valid 
                                @RequestBody TaskStatusUpdateRequest request) {
        return taskService.updateTaskStatus(id, request);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id,
                            @Valid @RequestBody TaskUpdateRequest request) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}