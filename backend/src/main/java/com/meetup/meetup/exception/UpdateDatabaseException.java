package com.meetup.meetup.exception;

public class UpdateDatabaseException extends RuntimeException {
    public UpdateDatabaseException(String entity) {
        super(String.format("Cannot update database with %s", entity));
    }

    public UpdateDatabaseException(Object entity) {
        super(String.format("Cannot update database with %s", entity.toString()));
    }
}
