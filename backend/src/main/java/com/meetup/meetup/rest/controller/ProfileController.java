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
    public ResponseEntity<User> updateProfile(@RequestBody User newUser) {
        log.debug("Trying to update user '{}'", newUser.toString());

        User updatedUser = profileService.updateUser(newUser);
        if (updatedUser != null) {
            log.debug("Send response body user '{}' and status OK", updatedUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        log.debug("Updating user '{}' failed", newUser.toString());
        return new ResponseEntity<>(newUser, HttpStatus.NOT_MODIFIED);
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
            return new ResponseEntity<>(HttpStatus.OK);
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

    @GetMapping("/userRelations/{userId}")
    public ResponseEntity<String> userRelations(@PathVariable() int userId){
        log.debug("Trying to get relation between user with id '{}' and authenticated user", userId);

        String relation = profileService.userRelations(userId);

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

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String username, @RequestParam(name = "type") String typeOfRelationship) {
        log.debug("Trying to search users by username '{}'",
                username);

        List<User> users = profileService.getUsersByRelationshipType(username, typeOfRelationship);

        log.debug("Found users '{}'", users.toString());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/withEvent/{login}")
    public ResponseEntity<User> getProfileWithEvent(@PathVariable String login){
        log.debug("Trying to get user by login '{}'", login);

        User user = profileService.getUserByLogin(login);

        Event event = null;
        log.debug("Trying to get event by id '{}'", user.getPinedEventId());

        if(user.getPinedEventId() != 0) {
            event= eventService.getEvent(user.getPinedEventId());
        } else{
            log.debug("There is no pined event");
        }

        log.debug("setting user and event info to response entity");
        if(event!=null) {
            user.setPinedEventDate(event.getEventDate());
            user.setPinedEventName(event.getName());
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}