package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.ProfileService;
import com.meetup.meetup.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final StorageService storageService;

    @Autowired
    public ProfileController(ProfileService profileService, StorageService storageService) {
        this.profileService = profileService;
        this.storageService = storageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return new ResponseEntity<>(profileService.getUser(id), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody User newUser) {
        User updatedUser = profileService.updateUser(newUser);
        if (updatedUser != null) {
            return new ResponseEntity<>("Successfully updated profile", HttpStatus.OK);
        }
        return new ResponseEntity<>("Updating failed", HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<User>> getFriends() {
        return new ResponseEntity<>(profileService.getFriends(), HttpStatus.OK);
    }

    @GetMapping("/friendsRequests")
    public ResponseEntity<List<User>> getFriendsRequests() {
        return new ResponseEntity<>(profileService.getFriendsRequests(), HttpStatus.OK);
    }

    @PostMapping("/addFriend")
    public ResponseEntity<String> addFriend(@RequestBody String newFriend) {
        if (profileService.addFriend(newFriend)) {
            return new ResponseEntity<>("Successfully send request to " + newFriend, HttpStatus.OK);
        }
        return new ResponseEntity<>("User does not exist or you have already sent request", HttpStatus.EXPECTATION_FAILED);
    }

    @DeleteMapping("/deleteFriend")
    public ResponseEntity deleteFriend(@RequestBody int id) {
        profileService.deleteFriend(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/confirmFriend")
    public ResponseEntity confirmFriend(@RequestBody int friendId) {
        profileService.confirmFriend(friendId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String message;
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