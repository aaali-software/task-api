package com.aziz.taskapi.dto;

import java.time.LocalDateTime;

import com.aziz.taskapi.enums.TaskPriority;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO class for creating a new Task.
 * This class contains fields for the title, description, priority, and due date of a Task, along with validation annotations
 * to ensure that the required fields are provided and meet certain constraints (e.g., title length, future due date).
 * I created the TaskCreateRequest class to provide a clear and specific structure for requests that aim to create a new Task. 
 * This allows for better validation and separation of concerns, as the request for creating a Task is distinct from other 
 * types of requests (e.g., updating a Task's status). The class includes getters and setters for all fields to facilitate 
 * its usage in controller methods that handle Task creation.
 */
public class TaskCreateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDateTime dueDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
    
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}