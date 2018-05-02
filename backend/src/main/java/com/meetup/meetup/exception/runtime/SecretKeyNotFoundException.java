package com.meetup.meetup.exception.runtime;

public class SecretKeyNotFoundException extends CustomRuntimeException {

    public SecretKeyNotFoundException(String message) {
        super(message);
    }

}
