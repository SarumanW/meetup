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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PropertySource("classpath:strings.properties")
@RequestMapping("/api/wish.list")
public class WishListController {

    private static Logger log = LoggerFactory.getLogger(FolderController.class);

    @Autowired
    private WishListService wishListService;

    @GetMapping
    public ResponseEntity<List<Item>> getWishList(){
        log.debug("Trying to get all user items");

        List<Item> items = wishListService.getUserWishList();

        log.debug("Send response body items '{}' and status OK", items.toString());

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

}