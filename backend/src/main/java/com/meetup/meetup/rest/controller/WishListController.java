package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Item;
import com.meetup.meetup.service.WishListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/wishes")
public class WishListController {

    private static Logger log = LoggerFactory.getLogger(WishListController.class);

    @Autowired
    private WishListService wishListService;

    @GetMapping
    @PreAuthorize("@wishListAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Item>> getWishList(@PathVariable int userId){
        log.debug("Trying to get wish list");

        List<Item> items = wishListService.getWishList(userId);

        log.debug("Send response body items '{}' and status OK", items.toString());

        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    @GetMapping("/{login}")
    @PreAuthorize("@wishListAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Item>> getWishesByUser(@PathVariable int userId, @PathVariable String login) {
        log.debug("Trying to get wishes by login '{}'", login);

        List<Item> userWishes = wishListService.getWishesByUser(login);

        log.debug("Send response body wishes '{}' and status OK", userWishes.toString());

        return new ResponseEntity<>(userWishes, HttpStatus.OK);
    }

    @PostMapping("/recommendations")
    @PreAuthorize("@wishListAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Item>> getRecommendations(@PathVariable int userId, @RequestBody String[] tagArray) {
        log.debug("Trying to get  recommend wishes");

        List<Item> items = wishListService.getRecommendations(tagArray);

        log.debug("Send response body items '{}' and status OK", items.toString());

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/bookings")
    @PreAuthorize("@wishListAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Item>> getBookingByUser(@PathVariable int userId) {
        log.debug("Trying to get booking wishes by userId '{}'", userId);

        List<Item> items = wishListService.getBookingByUser(userId);

        log.debug("Send response body items '{}' and status OK", items.toString());

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/tags/{tagPart}")
    @PreAuthorize("@wishListAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<String>> getSearchTags(@PathVariable int userId, @PathVariable String tagPart) {
        log.debug("Trying to get tags by part name '{}'", tagPart);

        List<String> tags = wishListService.getSearchTags(tagPart);

        log.debug("Send response body tags '{}' and status OK", tags.toString());

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}