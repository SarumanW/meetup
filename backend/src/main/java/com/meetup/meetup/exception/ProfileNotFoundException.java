package com.meetup.meetup.exception;

import org.springframework.context.annotation.PropertySource;

import static java.lang.String.format;

public class ProfileNotFoundException extends RuntimeException {

    public ProfileNotFoundException(String username) {
        super(format("Profile with username %s does not exist", username));
    }
}
