package com.meetup.meetup.exception.runtime;

public class AuthenticationException extends CustomRuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
