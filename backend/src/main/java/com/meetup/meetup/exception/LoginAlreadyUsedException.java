package com.meetup.meetup.exception;


public class LoginAlreadyUsedException  extends RuntimeException {
    public LoginAlreadyUsedException() {
        super("Login already used!");
    }
}