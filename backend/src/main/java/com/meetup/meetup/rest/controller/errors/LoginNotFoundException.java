package com.meetup.meetup.rest.controller.errors;

public class LoginNotFoundException extends RuntimeException{
    public LoginNotFoundException() {
        super("SendCustomErrorLogin was not found");
    }
}
