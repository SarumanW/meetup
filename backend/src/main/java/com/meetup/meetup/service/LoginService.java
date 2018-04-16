package com.meetup.meetup.service;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.vm.LoginProfile;
import com.meetup.meetup.service.vm.MinimalProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginService {

    @Autowired
    private ProfileService profileService;

    public MinimalProfile login(LoginProfile credentials) {
        User user = profileService.get(credentials.getUsername());
        if (user != null && user.getPassword().equals(credentials.getPassword()))
            return new MinimalProfile(user);

        return null;
    }
}
