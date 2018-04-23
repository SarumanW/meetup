package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.AccountService;
import com.meetup.meetup.service.vm.LoginVM;
import com.meetup.meetup.service.vm.RecoveryPasswordVM;
import com.meetup.meetup.service.vm.UserAndTokenVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Configuration
@RestController
@PropertySource("classpath:strings.properties")
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public UserAndTokenVM login(@Valid @RequestBody LoginVM loginModel,
                                HttpServletResponse response) throws Exception {
        UserAndTokenVM userAndTokenVM = (UserAndTokenVM) accountService.login(loginModel);
        response.setHeader("Token", userAndTokenVM.getToken());
        return userAndTokenVM;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@Valid @RequestBody User user) throws Exception {
        return accountService.register(user);
    }

    @GetMapping("/recovery/{email}")
    public ResponseEntity<String> mailRecoveryPassword(@PathVariable String email) throws Exception {
        return accountService.recoveryPasswordMail(email);
    }

    @PostMapping("/recovery")
    public ResponseEntity<String> passwordRecovery(@Valid @RequestBody RecoveryPasswordVM model) throws Exception{
        return accountService.recoveryPassword(model);
    }
}