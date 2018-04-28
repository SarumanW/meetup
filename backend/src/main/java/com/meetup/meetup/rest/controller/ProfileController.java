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

    @GetMapping("/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        return new ResponseEntity<>(profileService.getUserByLogin(login), HttpStatus.OK);
    }

    @PutMapping("/update")
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

    @GetMapping("/friends/requests")
    public ResponseEntity<List<User>> getFriendsRequests() {
        return new ResponseEntity<>(profileService.getFriendsRequests(), HttpStatus.OK);
    }

    @PostMapping("/friends/add/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable int friendId) {
        if (profileService.addFriend(friendId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("User does not exist or you have already sent request", HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping("/friends/confirm/{friendId}")
    public ResponseEntity confirmFriend(@PathVariable int friendId) {
        profileService.confirmFriend(friendId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/friends/{friendId}")
    public ResponseEntity deleteFriend(@PathVariable int friendId) {
        profileService.deleteFriend(friendId);
        return new ResponseEntity(HttpStatus.OK);
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