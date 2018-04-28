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

import javax.jws.soap.SOAPBinding;
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
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        log.debug("Trying to get user by id '{}'", id);

        User user = profileService.getUser(id);

        log.debug("Send response body user '{}' and status OK", user.toString());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateProfile(@RequestBody User newUser) {
        log.debug("Trying to update user '{}'", newUser.toString());

        User updatedUser = profileService.updateUser(newUser);

        log.debug("Send response body user '{}' and status OK", updatedUser);

        if (updatedUser != null) {
            return new ResponseEntity<>("Successfully updated profile", HttpStatus.OK);
        }

        return new ResponseEntity<>("Updating failed", HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<User>> getFriends() {
        log.debug("Trying to get friends of authenticated user");

        List<User> friends = profileService.getFriends();

        log.debug("Send response body friends '{}' and status OK", friends);

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/friends/requests")
    public ResponseEntity<List<User>> getFriendsRequests() {
        log.debug("Trying to get requests from friends of authenticated user");

        List<User> friendRequests = profileService.getFriendsRequests();

        log.debug("Send response body friends requests '{}' and status OK", friendRequests);

        return new ResponseEntity<>(friendRequests, HttpStatus.OK);
    }

    @PostMapping("/friends/add/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable int friendId) {
        log.debug("Trying to add friend for authenticated user by friendId '{}'", friendId);

        if (profileService.addFriend(friendId)) {
            log.debug("Send response status OK");
            return new ResponseEntity<>(HttpStatus.OK);
        }

        log.debug("Send response status OK");

        return new ResponseEntity<>("User does not exist or you have already sent request", HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping("/friends/confirm/{friendId}")
    public ResponseEntity confirmFriend(@PathVariable int friendId) {
        log.debug("Trying to confirm friend for authenticated user by friendId '{}'", friendId);

        profileService.confirmFriend(friendId);

        log.debug("Send response status CREATED");

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/friends/{friendId}")
    public ResponseEntity deleteFriend(@PathVariable int friendId) {
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