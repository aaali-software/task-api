package com.aziz.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Main application class for the Task API.
 * This class bootstraps the Spring Boot application and defines the OpenAPI documentation for the API.
 * The OpenAPI definition includes the title, version, and description of the API for better documentation and client generation.
 * I used the @OpenAPIDefinition annotation to provide metadata for the API, which will be used by tools like Swagger UI to generate interactive API documentation.
 * This setup allows developers to easily understand and interact with the API endpoints, making it easier to integrate with other services or build client applications.
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Task API",
                version = "1.0",
                description = "Spring Boot REST API for managing tasks"
        )
)
public class TaskApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApiApplication.class, args);
    }
}