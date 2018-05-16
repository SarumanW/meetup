package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Item;
import com.meetup.meetup.service.WishListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PropertySource("classpath:strings.properties")
@RequestMapping("/api/wishes")
public class WishListController {

    private static Logger log = LoggerFactory.getLogger(WishListController.class);

    @Autowired
    private WishListService wishListService;

    @GetMapping
    public ResponseEntity<List<Item>> getWishList(){
        log.debug("Trying to get wish list");

        List<Item> items = wishListService.getWishList();

        log.debug("Send response body items '{}' and status OK", items.toString());

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{login}")
    public ResponseEntity<List<Item>> getWishesByUser(@PathVariable String login) {
        log.debug("Trying to get wishes by login '{}'", login);

        List<Item> userWishes = wishListService.getWishesByUser(login);

        log.debug("Send response body wishes '{}' and status OK", userWishes.toString());

        return new ResponseEntity<>(userWishes, HttpStatus.OK);
    }

    @PostMapping("/recommendations")
    public ResponseEntity<List<Item>> getRecommendations(@RequestBody String[] tagArray) {
        log.debug("Trying to get  recommend wishes");

        List<Item> items = wishListService.getRecommendations(tagArray);

        log.debug("Send response body items '{}' and status OK", items.toString());

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Item>> getBookingByUser() {
        log.debug("Trying to get booking wishes by user");

        List<Item> items = wishListService.getBookingByUser();

        log.debug("Send response body items '{}' and status OK", items.toString());

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/tags/{tagPart}")
    public ResponseEntity<List<String>> getSearchTags(@PathVariable String tagPart) {
        log.debug("Trying to get tags by part name '{}'", tagPart);

        List<String> tags = wishListService.getSearchTags(tagPart);

        log.debug("Send response body tags '{}' and status OK", tags.toString());

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}