package com.meetup.meetup.rest.controller;

import com.meetup.meetup.DAO.OracleUserDAO;
import com.meetup.meetup.rest.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class UserController {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody User user) {
        OracleUserDAO oracleUserDAO = new OracleUserDAO();
        if (null == oracleUserDAO.findUserByUserName(user.getUsername())) {
            //return thisUserName is busy

        }
        if (null == oracleUserDAO.findUserByMail(user.getEmail())) {
            //return this email is busy
        }
        if (oracleUserDAO.insertUser(user) == -1) {
            //return false;
        }
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public void loginAccount(@Valid @RequestBody User user) {
        OracleUserDAO oracleUserDAO = new OracleUserDAO();
        if (null == oracleUserDAO.findUserByUserName(user.getUsername())) {
        //return error
        }
        User requestedUser = oracleUserDAO.findUserByMail(user.getUsername());
        if (!requestedUser.getPassword().equals(user.getPassword())) {
        //return error
        }
        Cookie loginCookie = new Cookie("userName", requestedUser.getUsername());
        //checking email and pass then log in user
    }
}