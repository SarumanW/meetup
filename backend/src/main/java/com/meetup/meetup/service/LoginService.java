package com.meetup.meetup.service;

import com.meetup.meetup.service.vm.LoginProfile;
import com.meetup.meetup.service.vm.MinimalProfile;
import com.meetup.meetup.service.vm.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginService {

    @Autowired
    private ProfileService profileService;

    public MinimalProfile login(LoginProfile credentials) {
        Profile profile = profileService.get(credentials.getUsername());
        if (profile != null && profile.getPassword().equals(credentials.getPassword()))
            return new MinimalProfile(profile);

        return null;
    }
}
