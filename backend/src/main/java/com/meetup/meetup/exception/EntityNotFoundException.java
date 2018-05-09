package com.meetup.meetup.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, String fieldName, String fieldValue) {
        super(String.format("%s with %s=%s was not found in database", entityName, fieldName, fieldValue));
    }

    public EntityNotFoundException(String entityName, String fieldName, int fieldValue) {
        super(String.format("%s with %s=%s was not found in database", entityName, fieldName, fieldValue));
    }
}
