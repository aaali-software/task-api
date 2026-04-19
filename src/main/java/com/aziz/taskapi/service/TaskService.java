package com.aziz.taskapi.service;

import com.aziz.taskapi.dto.PagedResponse;
import com.aziz.taskapi.dto.TaskCreateRequest;
import com.aziz.taskapi.dto.TaskResponse;
import com.aziz.taskapi.dto.TaskStatusUpdateRequest;
import com.aziz.taskapi.dto.TaskUpdateRequest;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;

/**
 * Defines the task management operations exposed to the web layer.
 */

public interface TaskService {

    /**
     * Returns a paged list of tasks with optional filters and sorting.
     */
    PagedResponse<TaskResponse> getAllTasks(
            TaskStatus status,
            TaskPriority priority,
            int page,
            int size,
            String sortBy,
            String direction);

    /**
     * Returns a task by its identifier.
     */
    TaskResponse getTaskById(Long id);

    /**
     * Creates a new task.
     */
    TaskResponse createTask(TaskCreateRequest request);

    /**
     * Updates only the status of an existing task.
     */
    TaskResponse updateTaskStatus(Long id, TaskStatusUpdateRequest request);

    /**
     * Updates the mutable fields of an existing task.
     */
    TaskResponse updateTask(Long id, TaskUpdateRequest request);

    /**
     * Deletes a task by its identifier.
     */
    void deleteTask(Long id);
}
