package com.aziz.taskapi.service;

import java.util.List;

import com.aziz.taskapi.dto.TaskCreateRequest;
import com.aziz.taskapi.dto.TaskResponse;
import com.aziz.taskapi.dto.TaskStatusUpdateRequest;
import com.aziz.taskapi.dto.TaskUpdateRequest;
import com.aziz.taskapi.enums.TaskPriority;
import com.aziz.taskapi.enums.TaskStatus;

/**
 * Service interface for Task operations.
 * This interface defines methods for retrieving all tasks and getting a task by its ID.
 * It serves as a contract for the TaskServiceImpl class, which implements these methods to interact with the TaskRepository and perform business logic related to Task entities.
 * I created a TaskService interface that declares methods for fetching all tasks and retrieving a task by its ID. 
 * This interface allows for a clear separation of concerns, as it abstracts the business logic from the controller layer.
 * This interface is implemented by TaskServiceImpl, which uses the TaskRepository to perform the actual data retrieval from the database.
 * Additionally, I added a createTask method that takes a TaskCreateRequest object and an updateTaskStatus method that takes a TaskStatusUpdateRequest object, 
 * allowing for the creation of new tasks and updating the status of existing tasks, respectively.
 * I also added an updateTaskStatus method that takes a TaskStatusUpdateRequest object, which can be used to update the status of a Task without affecting other fields.
 * I also added an updateTask method that takes a TaskUpdateRequest object, which can be used to update other fields of a Task (e.g., title, description, priority, due date) in the future if needed.
 * Finally, I added a deleteTask method that allows for the deletion of a Task by its ID, which can be implemented in the future to provide full CRUD functionality for Task entities.
 */

public interface TaskService {

    List<TaskResponse> getAllTasks(TaskStatus status, TaskPriority priority);

    TaskResponse getTaskById(Long id);

    TaskResponse createTask(TaskCreateRequest request);

    TaskResponse updateTaskStatus(Long id, TaskStatusUpdateRequest request);

    TaskResponse updateTask(Long id, TaskUpdateRequest request);

    void deleteTask(Long id);
}