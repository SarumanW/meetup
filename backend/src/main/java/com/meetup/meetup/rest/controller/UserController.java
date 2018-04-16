package com.meetup.meetup.rest.controller;

import com.meetup.meetup.dao.impl.UserDaoImpl;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.MailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserDaoImpl userDaoImpl;
    @Autowired
    private MailService mailService;


    private static String getMd5Hash(String line){ //function for get MD5 Hash
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(line.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "something went wrong";
    }


    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@Valid @RequestBody User user) {
        if (null != userDaoImpl.findByLogin(user.getLogin())) { //checking if user exist in system
            return Json.createObjectBuilder().add("error", "This username is busy").build().toString();
        }
        if (null != userDaoImpl.findByEmail(user.getEmail())) { //checking if email exist in system
            return Json.createObjectBuilder().add("error", "This email is busy").build().toString();
        }

        String notHashedPass = user.getPassword(); //hashing password
        user.setPassword(getMd5Hash(notHashedPass));

        if (userDaoImpl.insert(user) == -1) { //checking adding to DB
            return Json.createObjectBuilder().add("error", "Something went wrong").build().toString();
        }

        mailService.sendMail(user.getEmail(), "Registration successfully", String.format(MailService.templateRegister, user.getName(), user.getLogin(), user.getPassword()));
        return Json.createObjectBuilder().add("success", "Success").build().toString();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public String loginAccount(@Valid @RequestBody User user) {

        String value = Json.createObjectBuilder().add("error", "Login or password incorrect").build().toString();

        User requestedUser = userDaoImpl.findByLogin(user.getLogin());
        if (null == requestedUser) { //checking existence of user in system
            return value;
        }
        if (!requestedUser.getPassword().equals(getMd5Hash(user.getPassword()))) { //checking equals hash of password
            return value;
        }

        Key key = MacProvider.generateKey();
        return Jwts.builder().setSubject(user.getLogin()).signWith(SignatureAlgorithm.HS256,key).compact();
    }
}