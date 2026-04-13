package com.aziz.taskapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.aziz.taskapi.dto.PagedResponse;
import com.aziz.taskapi.dto.TaskCreateRequest;
import com.aziz.taskapi.dto.TaskResponse;
import com.aziz.taskapi.dto.TaskStatusUpdateRequest;
import com.aziz.taskapi.dto.TaskUpdateRequest;
import com.aziz.taskapi.entity.Task;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;
import com.aziz.taskapi.exception.ResourceNotFoundException;
import com.aziz.taskapi.repository.TaskRepository;

/**
 * Service implementation for Task operations.
 * This class implements the TaskService interface and provides methods to
 * retrieve all tasks and get a task by its ID.
 * It uses the TaskRepository to interact with the database and perform CRUD
 * operations on Task entities.
 * I implemented the TaskService interface in TaskServiceImpl, which uses the
 * TaskRepository to fetch tasks from the database.
 * The getAllTasks method retrieves all tasks, optionally filtering by status
 * and priority if those parameters are provided.
 * The getTaskById method retrieves a specific task by its ID, throwing a
 * ResourceNotFoundException if the task does not exist.
 * Additionally, I added a createTask method that takes a TaskCreateRequest
 * object, constructs a new Task entity, and saves it to the database using the
 * repository.
 * I also implemented the updateTaskStatus method, which updates the status of
 * an existing task based on the provided TaskStatusUpdateRequest.
 * I also added an updateTask method that allows updating other fields of a Task
 * (e.g., title, description, priority, due date) using a TaskUpdateRequest
 * object.
 * Finally, I implemented a deleteTask method that deletes a Task by its ID,
 * throwing an exception if the task is not found.
 */

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public PagedResponse<TaskResponse> getAllTasks(
            TaskStatus status,
            TaskPriority priority,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> taskPage;

        if (status != null && priority != null) {
            taskPage = taskRepository.findByStatusAndPriority(status, priority, pageable);
        } else if (status != null) {
            taskPage = taskRepository.findByStatus(status, pageable);
        } else if (priority != null) {
            taskPage = taskRepository.findByPriority(priority, pageable);
        } else {
            taskPage = taskRepository.findAll(pageable);
        }

        var content = taskPage.getContent()
                .stream()
                .map(this::mapToTaskResponse)
                .toList();

        return new PagedResponse<>(
                content,
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages(),
                taskPage.isLast());
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        return mapToTaskResponse(task);
    }

    @Override
    public TaskResponse createTask(TaskCreateRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToTaskResponse(savedTask);
    }

    @Override
    public TaskResponse updateTaskStatus(Long id, TaskStatusUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setStatus(request.getStatus());

        Task updatedTask = taskRepository.save(task);
        return mapToTaskResponse(updatedTask);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Task updatedTask = taskRepository.save(task);
        return mapToTaskResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
    }

    private TaskResponse mapToTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt());
    }
}