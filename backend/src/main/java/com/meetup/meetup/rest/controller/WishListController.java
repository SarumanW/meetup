package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Item;
import com.meetup.meetup.service.WishListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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

    @GetMapping("/userWishList")
    public @ResponseBody ResponseEntity<List<Item>> getWishList(){
        log.debug("Trying to get wish list");
        List<Item> items = wishListService.getWishList();

        log.debug("Send response body items '{}' and status OK", items.toString());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/addToWishList")
    public @ResponseBody ResponseEntity<Item> addWishItem(@Valid @RequestBody Item item) {
        log.debug("Trying to add wish item {}", item);
        Item addWishItem = wishListService.addWishItem(item);

        log.debug("Send response body saved item '{}' and status CREATED", addWishItem);
        return new ResponseEntity<>(addWishItem, HttpStatus.CREATED);
    }


}