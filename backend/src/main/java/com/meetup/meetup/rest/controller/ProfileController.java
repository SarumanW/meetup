package com.meetup.meetup.rest.controller;

import com.meetup.meetup.exception.ProfileNotFoundException;
import com.meetup.meetup.service.ProfileService;
import com.meetup.meetup.service.vm.DetailedProfile;
import com.meetup.meetup.service.vm.MinimalProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{login}")
    public MinimalProfile minimal(@PathVariable String login) {

        MinimalProfile minProfile =  profileService.minimal(login);

        if (minProfile == null)
            throw new ProfileNotFoundException(login);

        return minProfile;
    }

    @GetMapping("/details/{login}")
    public DetailedProfile details(@PathVariable String login) {
        DetailedProfile detailedProfile = profileService.detailed(login);

        if (detailedProfile == null)
            throw new ProfileNotFoundException(login);

        return detailedProfile;
    }
}
