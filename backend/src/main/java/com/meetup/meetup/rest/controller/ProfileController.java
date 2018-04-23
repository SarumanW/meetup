package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.ProfileService;
import com.meetup.meetup.entity.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return profileService.getUser(id);
    }

    @PostMapping("/update")
    public String updateProfile(@RequestBody User newUser){
        if(profileService.updateUser(newUser)){
            return "Success";
        }
        return "Don't updated";
    }

    @GetMapping("/friends")
    public List<Friend> getFriends(){
        // TODO: 21.04.2018 need FriendDao
        return profileService.getFriends();
    }

    @PostMapping("/addFriend")
    public String addFriend(@RequestBody String newFriend){
        // TODO: 21.04.2018 tests - receiving friend name from frontend, need FriendDao
        System.out.println(newFriend);
        return newFriend;
    }

    @PostMapping("/deleteFriend")
    public int deleteFriend(@RequestBody int id){
        // TODO: 21.04.2018 tests - receiving deleted friend name from frontend, need FriendDao
        System.out.println(id);
        return id;
    }
}