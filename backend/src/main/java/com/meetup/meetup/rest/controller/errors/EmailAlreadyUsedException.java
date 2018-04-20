package com.meetup.meetup.rest.controller.errors;


public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException() {
        super("SendCustomErrorEmail already used");
    }
}