package com.meetup.meetup.exception;

public class BadTokenException extends RuntimeException {
    public BadTokenException() {
        super("Token is not correct");
    }
}
