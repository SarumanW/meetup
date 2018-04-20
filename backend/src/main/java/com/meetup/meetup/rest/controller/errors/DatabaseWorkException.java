package com.meetup.meetup.rest.controller.errors;



public class DatabaseWorkException extends RuntimeException {
    public DatabaseWorkException() {
        super("SendCustomErrorDatabase work error");
    }
}
