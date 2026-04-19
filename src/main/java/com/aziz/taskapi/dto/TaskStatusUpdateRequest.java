package com.aziz.taskapi.dto;

import com.aziz.taskapi.enums.TaskStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload used to update only a task's status.
 */
@Getter
@Setter
@NoArgsConstructor
public class TaskStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private TaskStatus status;
}
