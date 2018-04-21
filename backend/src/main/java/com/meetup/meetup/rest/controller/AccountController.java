package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.AccountService;
import com.meetup.meetup.service.vm.Profile;
import com.meetup.meetup.service.vm.RecoveryPasswordProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
@Configuration
@RestController
@PropertySource("classpath:strings.properties")
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public Profile login(@Valid @RequestBody Profile credentials,
                         HttpServletResponse response) throws Exception {
        Profile minimalProfile = accountService.login(credentials);
        response.setHeader("Token", minimalProfile.getToken());
        return minimalProfile;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@Valid @RequestBody User user) throws Exception {
        return accountService.register(user);
    }

    // TODO: 4/19/2018 Implement password restore
    @GetMapping("/recovery/{email}")
    public ResponseEntity<String> mailRecoveryPassword(@PathVariable String email) throws Exception {
        return accountService.recoveryPasswordMail(email);
    }

    @PostMapping("/recovery")
    public ResponseEntity<String> passwordRecovery(@Valid @RequestBody RecoveryPasswordProfile profile) throws Exception{
        return accountService.recoveryPassword(profile);
    }
}
