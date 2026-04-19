package com.aziz.taskapi.exception;

/**
 * Signals that a requested resource could not be found.
 */

public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new exception with a descriptive message.
     *
     * @param message error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
