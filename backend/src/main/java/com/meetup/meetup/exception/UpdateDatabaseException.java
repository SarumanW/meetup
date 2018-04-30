package com.meetup.meetup.exception;

// TODO: 4/30/2018 Remove if is needless
public class UpdateDatabaseException extends RuntimeException {
    public UpdateDatabaseException(String entity) {
        super(String.format("Cannot update database with %s", entity));
    }

    public UpdateDatabaseException(Object entity) {
        super(String.format("Cannot update database with %s", entity.toString()));
    }
}
