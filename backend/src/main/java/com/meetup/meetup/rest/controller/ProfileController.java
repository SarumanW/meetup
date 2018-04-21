package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

    // TODO: 4/19/2018 Remove deprecated methods. Use only Profile class. Add new methods. All logic to service

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return profileService.getById(id);
    }

    @PostMapping("/update")
    public String updateProfile(@RequestBody User updatedProfile){
        if(profileService.updateProfile(updatedProfile)){
            return "Success";
        }
        return "Don't updated";
    }

    @PostMapping("/friends")
    public List<User> getFriends(HttpServletRequest request){
        // TODO: 20.04.2018 make logic getting token
        User profile = profileService.getProfileFromToken(request.getHeader("Authorization"));
        return profileService.getFriends(profile.getId());
    }
}
