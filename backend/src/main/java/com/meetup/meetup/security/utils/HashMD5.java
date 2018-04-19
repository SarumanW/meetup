package com.meetup.meetup.security.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashMD5 {
    public static String hash(String line) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(line.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
}
