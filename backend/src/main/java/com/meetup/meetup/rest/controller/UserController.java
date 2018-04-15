package com.meetup.meetup.rest.controller;

import com.meetup.meetup.DAO.OracleUserDAO;
import com.meetup.meetup.rest.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final int TABLE_NUMBER = 5;
    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@Valid @RequestBody User user) {
        OracleUserDAO oracleUserDAO = new OracleUserDAO();

        if (null == oracleUserDAO.findUserByUserName(user.getUsername())) { //checking if user exist in system
        return Json.createObjectBuilder().add("error", "This username is busy").build().toString();
        }
        if (null == oracleUserDAO.findUserByMail(user.getEmail())) { //checking if email exist in system
            return Json.createObjectBuilder().add("error", "This email is busy").build().toString();
        }
        if (oracleUserDAO.insertUser(user) == -1) { //checking adding to DB
            return Json.createObjectBuilder().add("error", "Something went wrong").build().toString();
        }
        user.setUserId(""+System.currentTimeMillis()+TABLE_NUMBER+(int)(Math.random()*100));
        oracleUserDAO.insertUser(user);
        return Json.createObjectBuilder().add("success", "Success").build().toString();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public String loginAccount(@Valid @RequestBody User user) {

        String value = Json.createObjectBuilder().add("error", "Login or password incorrect").build().toString();

        OracleUserDAO oracleUserDAO = new OracleUserDAO();
        User requestedUser = oracleUserDAO.findUserByUserName(user.getUsername());
        if (null == requestedUser) { //checking existence of user in system
            return value;
        }
        if (!requestedUser.getPassword().equals(user.getPassword())) { //checking equals of password
            return value;
        }
        return Json.createObjectBuilder().add("userId", requestedUser.getUserId()).build().toString();
    }
}