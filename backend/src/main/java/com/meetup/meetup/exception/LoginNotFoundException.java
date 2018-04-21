package com.meetup.meetup.exception;

public class LoginNotFoundException extends RuntimeException{
    public LoginNotFoundException() {
        super("Login was not found");
    }
}
