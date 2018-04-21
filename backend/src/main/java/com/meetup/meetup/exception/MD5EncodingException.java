package com.meetup.meetup.exception;

import org.springframework.context.annotation.PropertySource;

public class MD5EncodingException  extends RuntimeException {
    public MD5EncodingException() {
        super("SendCustomErrorEncoding password");
    }
}