package com.meetup.meetup.exception;


public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException() {
        super("SendCustomErrorEmail already used");
    }
}