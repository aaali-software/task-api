package com.aziz.taskapi.service;

import java.util.List;

import com.aziz.taskapi.entity.Task;

/**
 * Service interface for Task operations.
 * This interface defines methods for retrieving all tasks and getting a task by its ID.
 * It serves as a contract for the TaskServiceImpl class, which implements these methods to interact with the TaskRepository and perform business logic related to Task entities.
 * I created a TaskService interface that declares methods for fetching all tasks and retrieving a task by its ID. 
 * This interface is implemented by TaskServiceImpl, which uses the TaskRepository to perform the actual data retrieval from the database.
 */

public interface TaskService {

    List<Task> getAllTasks();

    Task getTaskById(Long id);
}