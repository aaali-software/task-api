package com.aziz.taskapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;

/**
 * Repository interface for Task entity.
 *
 * Extends JpaRepository to provide CRUD operations without manual SQL.
 * Includes custom query methods to find tasks by status, priority, or both.
 * This interface allows for easy data access and manipulation of Task entities in the database.
 * I defined a TaskRepository interface that extends JpaRepository, providing methods to find tasks by status and priority.
 * I used Spring Data JPA repositories, which allow defining query methods by convention, such as findByStatus or findByPriority, without writing explicit SQL.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find tasks by status.
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find tasks by priority.
     */
    List<Task> findByPriority(TaskPriority priority);

    /**
     * Find tasks by both status and priority.
     */
    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
}