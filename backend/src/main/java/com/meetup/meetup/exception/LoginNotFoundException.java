package com.meetup.meetup.exception;

public class LoginNotFoundException extends RuntimeException{
    public LoginNotFoundException() {
        super("SendCustomErrorLogin was not found");
    }
}
