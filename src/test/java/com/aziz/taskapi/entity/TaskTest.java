package com.aziz.taskapi.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.aziz.taskapi.enums.TaskStatus;

class TaskTest {

    @Test
    void shouldPreserveExistingStatusOnCreate() {
        Task task = Task.builder()
                .status(TaskStatus.COMPLETED)
                .build();

        task.onCreate();

        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    void shouldDefaultMissingStatusToPendingOnCreate() {
        Task task = Task.builder().build();

        task.onCreate();

        assertEquals(TaskStatus.PENDING, task.getStatus());
    }
}
