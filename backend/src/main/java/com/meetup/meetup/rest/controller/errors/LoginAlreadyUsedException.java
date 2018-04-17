package com.meetup.meetup.rest.controller.errors;

public class LoginAlreadyUsedException  extends RuntimeException {
    public LoginAlreadyUsedException() {
        super("Login already used");
    }
}