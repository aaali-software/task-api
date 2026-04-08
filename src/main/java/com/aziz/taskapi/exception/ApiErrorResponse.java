package com.aziz.taskapi.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * API error response class to standardize error responses across the application.
 * This class contains fields for timestamp, HTTP status code, error message, and validation errors (if any).
 * I created the ApiErrorResponse class to encapsulate error details in a consistent format for all API responses.
 * This allows for better error handling and debugging, as clients can easily understand the structure of error 
 * responses and extract relevant information when an error occurs. The class includes constructors, getters, 
 * and setters for all fields to facilitate its usage in exception handling scenarios.
 */

public class ApiErrorResponse {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> validationErrors;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(LocalDateTime timestamp, int status, String error, String message,
        Map<String, String> validationErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
