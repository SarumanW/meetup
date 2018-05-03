package com.meetup.meetup.exception.runtime.frontend.detailed;

public class LoginNotFoundException extends FrontendDetailedException{
    public LoginNotFoundException(String message) {
        super(message);
    }
}
