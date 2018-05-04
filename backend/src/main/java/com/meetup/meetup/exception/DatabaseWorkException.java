package com.meetup.meetup.exception;



public class DatabaseWorkException extends RuntimeException {
    public DatabaseWorkException() {
        super("SendCustomErrorDatabase work error");
    }
}
