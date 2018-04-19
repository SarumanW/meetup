package com.meetup.meetup.rest.controller.errors;

import static java.lang.String.format;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException() {
        super("Email already used");
    }
}