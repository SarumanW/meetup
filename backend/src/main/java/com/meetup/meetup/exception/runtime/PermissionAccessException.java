package com.meetup.meetup.exception.runtime;

public class PermissionAccessException extends RuntimeException {
    public PermissionAccessException(String message) {
        super(message);
    }
}
