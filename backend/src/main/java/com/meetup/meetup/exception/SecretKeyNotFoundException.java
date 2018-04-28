package com.meetup.meetup.exception;

public class SecretKeyNotFoundException extends Exception {
    public SecretKeyNotFoundException() {
    }

    public SecretKeyNotFoundException(String message) {
        super(message);
    }

    public SecretKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecretKeyNotFoundException(Throwable cause) {
        super(cause);
    }

    public SecretKeyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
