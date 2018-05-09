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

    private static Logger log = LoggerFactory.getLogger(FolderController.class);

    @Autowired
    private WishListService wishListService;

    @GetMapping
    public @ResponseBody ResponseEntity<List<Item>> getWishList(){
        log.debug("Trying to get wish list");
        List<Item> items = wishListService.getWishList();

        log.debug("Send response body items '{}' and status OK", items.toString());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/add")
    public @ResponseBody ResponseEntity<Item> addWishItem(@Valid @RequestBody Item item) {
        log.debug("Trying to add wish item {}", item);
        Item addWishItem = wishListService.addWishItem(item);

        log.debug("Send response body saved item '{}' and status CREATED", addWishItem);
        return new ResponseEntity<>(addWishItem, HttpStatus.CREATED);
    }

    @GetMapping("/{login}")
    public ResponseEntity<List<Item>> getWishesByUser(@PathVariable String login,@RequestParam(value="tag",required = false) String[] tagArray) {
        log.debug("Trying to get wishes by login '{}'", login);

        List<Item> userWishes = wishListService.getWishesByUser(login, tagArray);

        log.debug("Send response body wishes '{}' and status OK", userWishes.toString());

        return new ResponseEntity<>(userWishes, HttpStatus.OK);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Item>> getRecommendations(@RequestParam(value="tag") String[] tagArray) {
        log.debug("Trying to get  recommend wishes");
        List<Item> items = wishListService.getRecommendations(tagArray);

        log.debug("Send response body items '{}' and status OK", items.toString());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/bookings/{login}")
    public ResponseEntity<List<Item>> getBookingByUser(@PathVariable String login, @RequestParam(value="tag") String[] tagArray) {
        log.debug("Trying to get booking wishes by user login '{}'", login);
        List<Item> items = wishListService.getBookingByUser(login, tagArray);

        log.debug("Send response body items '{}' and status OK", items.toString());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }





}