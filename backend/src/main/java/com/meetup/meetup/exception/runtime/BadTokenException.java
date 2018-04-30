package com.meetup.meetup.exception.runtime;

import com.meetup.meetup.exception.runtime.CustomRuntimeException;

public class BadTokenException extends CustomRuntimeException {
    public BadTokenException(String message) {
        super(message);
    }
}
