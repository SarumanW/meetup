package com.meetup.meetup.rest.controller.errors;

public class MD5EncodingException  extends RuntimeException {
    public MD5EncodingException() {
        super("Encoding password");
    }
}