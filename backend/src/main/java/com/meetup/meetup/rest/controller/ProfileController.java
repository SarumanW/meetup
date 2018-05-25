package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.EventService;
import com.meetup.meetup.service.ProfileService;
import com.meetup.meetup.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

    private static Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;
    private final StorageService storageService;
    private final EventService eventService;

    @Autowired
    public ProfileController(ProfileService profileService, StorageService storageService, EventService eventService) {
        this.profileService = profileService;
        this.storageService = storageService;
        this.eventService = eventService;
    }

    @GetMapping("/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        log.debug("Trying to get user by login '{}'", login);

        User user = profileService.getUserByLogin(login);

        log.debug("Send response body user '{}' and status OK", user.toString());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/login/{id}")
    public ResponseEntity<Object> getLoginById(@PathVariable int id) {
        log.debug("Trying to get user's login by id '{}'", id);

        String login = profileService.getUserLoginById(id);

        log.debug("Send response body login '{}' and status OK", login);

        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("@profileAuthorization.isUserCorrect(#user.id)")
    public ResponseEntity<User> updateProfile(@RequestBody User user) {
        log.debug("Trying to update user '{}'", user.toString());

        User updatedUser = profileService.updateUser(user);

        log.debug("Send response body user '{}' and status OK", updatedUser);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{login}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable String login) {
        log.debug("Trying to get friends of authenticated user");

        List<User> friends = profileService.getFriends(login);

        log.debug("Send response body friends '{}' and status OK", friends);

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/{userId}/friends/requests")
    @PreAuthorize("@profileAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<User>> getFriendsRequests(@PathVariable int userId) {
        log.debug("Trying to get requests from friends of authenticated user");

        List<User> friendRequests = profileService.getFriendsRequests(userId);

        log.debug("Send response body friends requests '{}' and status OK", friendRequests);

        return new ResponseEntity<>(friendRequests, HttpStatus.OK);
    }

    @PostMapping("/{userId}/friends")
    @PreAuthorize("@profileAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<String> addFriend(@PathVariable int userId, @RequestBody String friendLogin) {
        log.debug("Trying to add friend for authenticated user by friendLogin '{}'", friendLogin);

        if (profileService.addFriend(userId, friendLogin)) {

            log.debug("Friend successfully added");
            log.debug("Send response status OK");

            return new ResponseEntity<>(HttpStatus.OK);
        }

        log.debug("Send response status EXPECTATION_FAILED");

        return new ResponseEntity<>("User does not exist or you have already sent request", HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping("{userId}/friends/confirm")
    @PreAuthorize("@profileAuthorization.isUserCorrect(#userId)")
    public ResponseEntity confirmFriend(@PathVariable int userId, @RequestBody int friendId) {
        log.debug("Trying to confirm friend for authenticated user by friendId '{}'", friendId);

        profileService.confirmFriend(userId, friendId);

        log.debug("Send response status CREATED");

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @PreAuthorize("@profileAuthorization.isUserCorrect(#userId)")
    public ResponseEntity deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.debug("Trying to delete friend for authenticated user by friendId '{}'", friendId);

        profileService.deleteFriend(userId, friendId);

        log.debug("Send response status OK");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("{userId}/relations/{otherUserId}")
    public ResponseEntity<String> userRelations(@PathVariable int userId, @PathVariable int otherUserId){
        log.debug("Trying to get relation between user with id '{}' and authenticated user '{}'", otherUserId, userId);

        String relation = profileService.userRelations(userId, otherUserId);

        log.debug("Send response body relation '{}' and status OK", relation);

        return new ResponseEntity<>(relation, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.debug("Trying to upload image '{}'", file);

        User updatedUser = storageService.store(file);

        log.debug("Image successfully uploaded send response status OK");

        return new ResponseEntity<>(updatedUser.getImgPath(), HttpStatus.OK);
    }

    @GetMapping("/{userId}/search")
    @PreAuthorize("@profileAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<User>> searchUsers(@PathVariable int userId, @RequestParam String username, @RequestParam(name = "type") String typeOfRelationship) {
        log.debug("Trying to search users by username '{}'", username);

        List<User> users = profileService.getUsersByRelationshipType(userId, username, typeOfRelationship);

        log.debug("Found users '{}' and send response status OK", users.toString());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{login}/event/pined")
    public ResponseEntity<User> getProfileWithEvent(@PathVariable String login){
        log.debug("Trying to get user with event by login '{}'", login);

        User user = profileService.getProfileWithEvent(login);

        log.debug("Received user '{}' and send response status OK", user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}