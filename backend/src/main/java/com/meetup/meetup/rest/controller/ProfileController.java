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

    @GetMapping("/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        log.debug("Trying to get user by login '{}'", login);

        User user = profileService.getUserByLogin(login);

        log.debug("Send response body user '{}' and status OK", user.toString());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateProfile(@RequestBody User newUser) {
        log.debug("Trying to update user '{}'", newUser.toString());

        User updatedUser = profileService.updateUser(newUser);

        if (updatedUser != null) {
            log.debug("Send response body user '{}' and status OK", updatedUser);
            return new ResponseEntity<>("Successfully updated profile", HttpStatus.OK);
        }
        log.debug("Updating user '{}' failed", newUser.toString());
        return new ResponseEntity<>("Updating failed", HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/{login}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable String login) {
        log.debug("Trying to get friends of authenticated user");

        List<User> friends = profileService.getFriends(login);

        log.debug("Send response body friends '{}' and status OK", friends);

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/friendsRequests")
    public ResponseEntity<List<User>> getFriendsRequests() {
        log.debug("Trying to get requests from friends of authenticated user");

        List<User> friendRequests = profileService.getFriendsRequests();

        log.debug("Send response body friends requests '{}' and status OK", friendRequests);

        return new ResponseEntity<>(friendRequests, HttpStatus.OK);
    }

    @PostMapping("/addFriend")
    public ResponseEntity<String> addFriend(@RequestBody String friendLogin) {
        log.debug("Trying to add friend for authenticated user by friendLogin '{}'", friendLogin);

        if (profileService.addFriend(friendLogin)) {
            log.debug("Friend successfully added");
            log.debug("Send response status OK");
            return new ResponseEntity<>("Successfully send request to " + friendLogin, HttpStatus.OK);
        }

        log.debug("Send response status EXPECTATION_FAILED");

        return new ResponseEntity<>("User does not exist or you have already sent request", HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping("/confirmFriend")
    public ResponseEntity confirmFriend(@RequestBody int friendId) {
        log.debug("Trying to confirm friend for authenticated user by friendId '{}'", friendId);

        profileService.confirmFriend(friendId);

        log.debug("Send response status CREATED");

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/deleteFriend")
    public ResponseEntity deleteFriend(@RequestBody int friendId) {
        log.debug("Trying to delete friend for authenticated user by friendId '{}'", friendId);

        profileService.deleteFriend(friendId);

        log.debug("Send response status OK");

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.debug("Trying to upload image '{}'", file);

        String message;
        try {
            storageService.store(file);
            message = "You successfully uploaded " + file.getOriginalFilename() + "!";

            log.debug("Image successfully uploaded send response status OK");
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }
}