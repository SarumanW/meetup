package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.ProfileService;
import com.meetup.meetup.service.vm.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

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
    public List<Friend> getFriends(HttpServletRequest request){
        // TODO: 20.04.2018 make logic getting token
        Profile profile = profileService.getProfileFromToken(request.getHeader("Authorization"));
        return null;
    }

    @PostMapping("/addFriend")
    public String addFriend(@RequestBody String newFriend){
        // TODO: 21.04.2018 tests - receiving friend name from frontend
        System.out.println(newFriend);
        return newFriend;
    }

    @PostMapping("/deleteFriend")
    public String deleteFriend(@RequestBody String deletedFriend){
        // TODO: 21.04.2018 tests - receiving deleted friend name from frontend
        System.out.println(deletedFriend);
        return deletedFriend;
    }
}