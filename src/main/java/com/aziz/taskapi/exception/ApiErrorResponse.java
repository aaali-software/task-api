package com.aziz.taskapi.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error payload returned by the API.
 */

public class ApiErrorResponse {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> validationErrors;

    public ApiErrorResponse() {
    }

    /**
     * Creates an error payload.
     *
     * @param timestamp time when the error was produced
     * @param status HTTP status code
     * @param error HTTP reason phrase
     * @param message application-specific error message
     * @param validationErrors field-level validation errors when applicable
     */
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
