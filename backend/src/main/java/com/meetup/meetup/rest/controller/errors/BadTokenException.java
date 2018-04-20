package com.meetup.meetup.rest.controller.errors;

public class BadTokenException extends RuntimeException {
    public BadTokenException() {
        super("Token is not correct");
    }
}
