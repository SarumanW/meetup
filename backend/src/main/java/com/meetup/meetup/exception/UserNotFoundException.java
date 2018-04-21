package com.meetup.meetup.exception;

import org.springframework.context.annotation.PropertySource;

import static java.lang.String.format;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String fieldName, String username) {
        super(format("User with '%s'='%s' does not exist", fieldName, username));
    }
}
