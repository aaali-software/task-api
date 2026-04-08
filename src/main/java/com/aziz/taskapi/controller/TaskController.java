package com.aziz.taskapi.controller;

import com.aziz.taskapi.dto.TaskCreateRequest;
import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling Task-related requests.
 * This class provides REST endpoints for retrieving all tasks and getting a task by its ID.
 * It uses the TaskService to delegate the business logic and interact with the TaskRepository.
 * I created a TaskController class annotated with @RestController and @RequestMapping to define the base URL for task-related endpoints.
 * The controller has two endpoints: one for fetching all tasks (GET /api/tasks) and another for retrieving a specific task by its ID (GET /api/tasks/{id}).
 * Additionally, I added a POST endpoint to create a new task, which accepts a TaskCreateRequest object in the request body and returns the created Task with a 201 Created status.
 * The controller methods call the corresponding service methods to perform the necessary operations and return the results as JSON responses.
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
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@Valid @RequestBody TaskCreateRequest request) {
        return taskService.createTask(request);
    }
}