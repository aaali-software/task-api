package com.aziz.taskapi.entity;

import java.time.LocalDateTime;

import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a task in the system.
 * Each task has a title, description, status, priority, due date, and timestamps for creation and updates.
 * I modeled a Task entity with status and priority enums, persisted via JPA.
 */

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    private LocalDateTime dueDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    /**
     * Lifecycle callbacks to set timestamps and default status before persisting and updating the entity.
     * onCreate() sets the createdAt timestamp and initializes the status to PENDING when a new task is created.
     * onUpdate() updates the updatedAt timestamp whenever the task is modified.
     * This ensures that we have accurate tracking of when tasks are created and updated, and that new tasks start with a consistent status.
     * Avoids manual setting of these fields in the service layer, reducing potential errors and ensuring data integrity.
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = TaskStatus.PENDING;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}