package com.meetup.meetup.rest.controller;

import com.meetup.meetup.rest.controller.errors.FailedToLoginException;
import com.meetup.meetup.rest.controller.errors.MD5EncodingException;
import com.meetup.meetup.security.utils.HashMD5;
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
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "/api/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private JwtService jwtService;

    @PostMapping
    public MinimalProfile login(@Valid @RequestBody LoginProfile credentials,
                                HttpServletResponse response) {
        String md5Pass = HashMD5.hash(credentials.getPassword());

        if(md5Pass == null) throw new FailedToLoginException(credentials.getLogin());

        //credentials.setPassword(md5Pass);

        MinimalProfile minimalProfile = loginService.login(credentials);

        if (minimalProfile == null) throw new FailedToLoginException(credentials.getLogin());

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
