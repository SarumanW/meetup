package com.meetup.meetup.rest.controller;

import com.meetup.meetup.exception.FailedToLoginException;
import com.meetup.meetup.service.JwtService;
import com.meetup.meetup.service.LoginService;
import com.meetup.meetup.service.vm.LoginProfile;
import com.meetup.meetup.service.vm.MinimalProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping(path = "account/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private JwtService jwtService;

    @PostMapping
    public MinimalProfile login(@RequestBody LoginProfile credentials,
                                HttpServletResponse response) {
        MinimalProfile minimalProfile = loginService.login(credentials);
        if (minimalProfile == null)
            throw new FailedToLoginException(credentials.getUsername());

        String token = null;
        try {
            token = jwtService.tokenFor(minimalProfile);
            minimalProfile.setToken(token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        response.setHeader("Token", token);
        return minimalProfile;
    }
}
