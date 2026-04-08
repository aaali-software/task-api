package com.aziz.taskapi.service;

import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for Task operations.
 * This class implements the TaskService interface and provides methods to retrieve all tasks and get a task by its ID.
 * It uses the TaskRepository to interact with the database and perform CRUD operations on Task entities.
 * I implemented the TaskService interface in TaskServiceImpl, which uses the TaskRepository to fetch tasks from the database. 
 * The getAllTasks method retrieves all tasks, while getTaskById fetches a specific task by its ID, throwing an exception if not found.
 */

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }
}