package com.aziz.taskapi.dto;

import com.aziz.taskapi.enums.TaskStatus;

import jakarta.validation.constraints.NotNull;

/**
 * DTO class for updating the status of a Task.
 * This class contains a single field for the new status of the Task, along with validation annotations
 * to ensure that the status is not null when a request is made to update a Task's status.
 * I created the TaskStatusUpdateRequest class to provide a clear and specific structure for requests that 
 * aim to update the status of a Task. This allows for better validation and separation of concerns, as the 
 * request for updating a Task's status is distinct from other types of requests (e.g., creating a Task). 
 * The class includes a getter and setter for the status field to facilitate its usage in controller methods that handle status updates.
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
