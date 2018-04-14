package com.meetup.meetup.rest;

import com.meetup.meetup.rest.model.RegisterAccount;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountResource {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody RegisterAccount managedUser) {
        System.out.println(managedUser);
        //checking if email is unique then sing up user
    }

//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void loginAccount(@Valid @RequestBody LoginAccount managedUser) {
//        System.out.println(managedUser);
//        //checking email and pass then log in user
//    }
}