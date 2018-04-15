package com.meetup.meetup.rest.controller;

import com.meetup.meetup.dao.impl.UserDaoImpl;
import com.meetup.meetup.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class UserController {
    //    private static final int TABLE_NUMBER = 5;
    @Autowired
    private UserDaoImpl userDaoImpl;

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@Valid @RequestBody User user) {
//        UserDaoImpl userDaoImpl = new UserDaoImpl();
        System.out.println(user);
        if (null != userDaoImpl.findByLogin(user.getLogin())) { //checking if user exist in system
            return Json.createObjectBuilder().add("error", "This username is busy").build().toString();
        }
        if (null != userDaoImpl.findByEmail(user.getEmail())) { //checking if email exist in system
            return Json.createObjectBuilder().add("error", "This email is busy").build().toString();
        }
        //user.setUserId("" + System.currentTimeMillis() + TABLE_NUMBER + (int) (Math.random() * 100));
        if (userDaoImpl.insert(user) == -1) { //checking adding to DB
            return Json.createObjectBuilder().add("error", "Something went wrong").build().toString();
        }

        return Json.createObjectBuilder().add("success", "Success").build().toString();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public String loginAccount(@Valid @RequestBody User user) {

        String value = Json.createObjectBuilder().add("error", "Login or password incorrect").build().toString();

//        UserDaoImpl userDaoImpl = new UserDaoImpl();
        User requestedUser = userDaoImpl.findByLogin(user.getLogin());
        if (null == requestedUser) { //checking existence of user in system
            return value;
        }
        if (!requestedUser.getPassword().equals(user.getPassword())) { //checking equals of password
            return value;
        }
        return Json.createObjectBuilder().add("userId", requestedUser.getId()).build().toString();
    }
}