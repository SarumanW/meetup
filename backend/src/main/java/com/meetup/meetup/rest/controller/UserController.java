package com.meetup.meetup.rest.controller;

import com.meetup.meetup.DAO.OracleUserDAO;
import com.meetup.meetup.rest.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@Valid @RequestBody User user) {
        OracleUserDAO oracleUserDAO = new OracleUserDAO();

        if (null == oracleUserDAO.findUserByUserName(user.getUsername())) {
        return Json.createObjectBuilder().add("error", "This username is busy").build().toString();
        }
        if (null == oracleUserDAO.findUserByMail(user.getEmail())) {
            return Json.createObjectBuilder().add("error", "This email is busy").build().toString();
        }
        if (oracleUserDAO.insertUser(user) == -1) {
            return Json.createObjectBuilder().add("error", "Something went wrong").build().toString();
        }
        return Json.createObjectBuilder().add("success", "Success").build().toString();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public String loginAccount(@Valid @RequestBody User user) {
        OracleUserDAO oracleUserDAO = new OracleUserDAO();
        String value = Json.createObjectBuilder().add("error", "Login or password incorrect").build().toString();
        if (null == oracleUserDAO.findUserByUserName(user.getUsername())) {
            return value;
        }
        User requestedUser = oracleUserDAO.findUserByMail(user.getUsername());
        if (!requestedUser.getPassword().equals(user.getPassword())) {
            return value;
        }
        Cookie loginCookie = new Cookie("userName", requestedUser.getUsername());
        return "endfile";
        //checking email and pass then log in user
    }
}