package com.meetup.meetup.rest.controller;

import com.meetup.meetup.exception.ProfileNotFoundException;
import com.meetup.meetup.service.ProfileService;
import com.meetup.meetup.service.vm.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

    // TODO: 4/19/2018 Remove deprecated methods. Use only Profile class. Add new methods. All logic to service

    @Autowired
    private ProfileService profileService;

    @Deprecated
    @GetMapping("/{login}")
    public Profile minimal(@PathVariable String login) {

        Profile minProfile =  profileService.minimal(login);

        if (minProfile == null)
            throw new ProfileNotFoundException(login);

        return minProfile;
    }

    @Deprecated
    @GetMapping("/details/{login}")
    public Profile details(@PathVariable String login) {
        Profile detailedProfile = profileService.detailed(login);

        if (detailedProfile == null)
            throw new ProfileNotFoundException(login);

        return detailedProfile;
    }
}
