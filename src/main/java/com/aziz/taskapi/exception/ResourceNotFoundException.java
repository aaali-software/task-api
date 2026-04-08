package com.aziz.taskapi.exception;

/**
 * Custom exception class for resource not found scenarios.
 * This exception is thrown when a requested resource (e.g., Task) is not found in the database.
 * I created the ResourceNotFoundException class to provide a specific exception type for cases where a resource cannot be found, 
 * allowing for more precise error handling and clearer communication of the error to clients. 
 * This exception can be used in service methods to indicate that a requested entity does not exist, and it can be caught in a global exception handler to return an appropriate HTTP response.
 */

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
