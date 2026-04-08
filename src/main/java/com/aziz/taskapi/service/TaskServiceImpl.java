package com.aziz.taskapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aziz.taskapi.dto.TaskCreateRequest;
import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.exception.ResourceNotFoundException;
import com.aziz.taskapi.repository.TaskRepository;
import com.aziz.taskapi.dto.TaskStatusUpdateRequest;

/**
 * Service implementation for Task operations.
 * This class implements the TaskService interface and provides methods to retrieve all tasks and get a task by its ID.
 * It uses the TaskRepository to interact with the database and perform CRUD operations on Task entities.
 * I implemented the TaskService interface in TaskServiceImpl, which uses the TaskRepository to fetch tasks from the database. 
 * The getAllTasks method retrieves all tasks, while getTaskById fetches a specific task by its ID, throwing an exception if not found.
 * Additionally, I added a createTask method that takes a TaskCreateRequest object, constructs a new Task entity, and saves it to the database using the repository.
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
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    @Override
    public Task createTask(TaskCreateRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .build();

        return taskRepository.save(task);
    }

    @Override
    public Task updateTaskStatus(Long id, TaskStatusUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setStatus(request.getStatus());
        
        return taskRepository.save(task);
    }
}