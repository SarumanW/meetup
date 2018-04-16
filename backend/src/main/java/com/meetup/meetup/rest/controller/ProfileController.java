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
@RequestMapping(path = "/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{username}")
    public MinimalProfile minimal(@PathVariable String username) {

        MinimalProfile minProfile =  profileService.minimal(username);

        if (minProfile == null)
            throw new ProfileNotFoundException(username);

        return minProfile;
    }

    @GetMapping("/details/{username}")
    public DetailedProfile details(@PathVariable String username) {
        DetailedProfile detailedProfile = profileService.detailed(username);

        if (detailedProfile == null)
            throw new ProfileNotFoundException(username);

        return detailedProfile;
    }
}
