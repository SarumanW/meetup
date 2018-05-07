package com.meetup.meetup.rest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.ItemPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meetup.meetup.service.ItemService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/item")
public class ItemController {

    private static Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Item> getItemById(@PathVariable int id) {
        log.debug("Try to get item with id '{}'", id);
        Item item = itemService.getItemById(id);

        log.debug("Send response body item '{}' and status OK", item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<Item> addItem(@Valid @RequestBody Item item) {
        log.debug("Trying to save item '{}'", item);
        Item addedItem = itemService.addItem(item);

        // TODO: 07.05.2018  
//        log.debug("Trying to save item with id '{}' to user wish list", addedItem.getItemId());
//        addedItem = itemService.addItemToUserWishList(addedItem.getItemId(), item);

        log.debug("Send response body saved item '{}' and status CREATED", addedItem);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/add")
    public @ResponseBody
    ResponseEntity<Item> addItemToUserWishList(@PathVariable int id, @Valid @RequestBody Item item) {
        log.debug("Trying to add item with id '{}' to user wish list", id);

        Item addedItem = itemService.addItemToUserWishList(id, item);
        log.debug("Item was added with id '{}'", addedItem.getItemId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public @ResponseBody
    ResponseEntity deleteItem(@PathVariable int id) {
        log.debug("Trying to delete item with id '{}' to user wish list", id);
        Item deletedItem = itemService.deleteItemFromUserWishList(id);

        log.debug("Send response status OK");
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public @ResponseBody
    ResponseEntity<Item> addLike(@PathVariable int id) {
        log.debug("Trying to add like to item with id '{}'", id);

        Item likedItem = itemService.addLike(id);
        log.debug("Item was added with id '{}'", id);
        return new ResponseEntity<>(likedItem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like")
    public @ResponseBody
    ResponseEntity<Item> removeLike(@PathVariable int id) {
        log.debug("Trying to remove like from item with id '{}'", id);

        Item unlikedItem = itemService.removeLike(id);
        log.debug("Item was added with id '{}'", id);
        return new ResponseEntity<>(unlikedItem, HttpStatus.OK);
    }

    @PutMapping
    public @ResponseBody
    ResponseEntity<Item> updateItem(@Valid @RequestBody Item newItem) {
        log.debug("Trying to update item '{}'", newItem);
        Item updatedItem = itemService.updateItem(newItem);

        log.debug("Send response body updated '{}' and status OK");
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public @ResponseBody
    ResponseEntity deleteItem(@Valid @RequestBody Item item) {
        log.debug("Trying to delete item '{}'", item);
        Item deletedItem = itemService.deleteItem(item);

        log.debug("Send response status OK");
        return new ResponseEntity(HttpStatus.OK);
    }
}
