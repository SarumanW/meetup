package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.AccountService;
import com.meetup.meetup.service.vm.LoginVM;
import com.meetup.meetup.service.vm.RecoveryPasswordVM;
import com.meetup.meetup.service.vm.UserAndTokenVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@PropertySource("classpath:strings.properties")
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<UserAndTokenVM> login(@Valid @RequestBody LoginVM loginModel) throws Exception {
        UserAndTokenVM userAndTokenVM = accountService.login(loginModel);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Token", userAndTokenVM.getToken());
        return new ResponseEntity<>(userAndTokenVM, headers, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity registerAccount(@Valid @RequestBody User user) throws Exception {
        accountService.register(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/recovery/{email}")
    public ResponseEntity mailRecoveryPassword(@PathVariable String email) throws Exception {
        accountService.recoveryPasswordMail(email);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping("/recovery")
    public ResponseEntity passwordRecovery(@Valid @RequestBody RecoveryPasswordVM model) throws Exception{
        accountService.recoveryPassword(model);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
