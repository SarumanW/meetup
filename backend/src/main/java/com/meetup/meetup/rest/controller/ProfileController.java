package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.ProfileService;
import com.meetup.meetup.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

    private static Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;
    private final StorageService storageService;

    @Autowired
    public ProfileController(ProfileService profileService, StorageService storageService) {
        this.profileService = profileService;
        this.storageService = storageService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return profileService.getUser(id);
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam(value = "login", required = false) String login) {
        log.debug("Trying to search users by login '{}', name '{}', surname '{}' and limit '{}'",
                login);

        List<User> users = profileService.getUnknownUsers(login);

        log.debug("Found users '{}'", users.toString());

        return users;
    }

    @PostMapping("/update")
    public String updateProfile(@RequestBody User newUser) {
        User updatedUser = profileService.updateUser(newUser);
        if (updatedUser != null) {
            return "Success";
        }
        return "Updating failed";
    }

    @GetMapping("/friends")
    public List<User> getFriends() {
        return profileService.getFriends();
    }

    @GetMapping("/friendsRequests")
    public List<User> getFriendsRequests() {
        return profileService.getFriendsRequests();
    }

    @PostMapping("/addFriend")
    public String addFriend(@RequestBody String newFriend) {
        if (profileService.addFriend(newFriend)) {
            return "Success";
        }
        return "Adding new friend failed";
    }

    @PostMapping("/deleteFriend")
    public int deleteFriend(@RequestBody int id) {
        return profileService.deleteFriend(id);
    }

    @PostMapping("/confirmFriend")
    public int confirmFriend(@RequestBody int friendId) {
        return profileService.confirmFriend(friendId);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.store(file);

            message = "You successfully uploaded " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);

        }
    }
}