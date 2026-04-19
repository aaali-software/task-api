package com.aziz.taskapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;

/**
 * Repository for querying and persisting {@link Task} entities.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Returns tasks matching the supplied status.
     */
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    /**
     * Returns tasks matching the supplied priority.
     */
    Page<Task> findByPriority(TaskPriority priority, Pageable pageable);

    /**
     * Returns tasks matching both status and priority.
     */
    Page<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority, Pageable pageable);
}
