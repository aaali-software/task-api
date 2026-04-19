package com.aziz.taskapi.dto;

import com.aziz.taskapi.enums.TaskStatus;

import jakarta.validation.constraints.NotNull;

/**
 * Request payload used to update only a task's status.
 */
public class TaskStatusUpdateRequest {
    
    @NotNull(message = "Status is required")
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

}
