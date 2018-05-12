package com.meetup.meetup.rest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.ItemPriority;
import com.meetup.meetup.service.EventImageService;
import com.meetup.meetup.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meetup.meetup.service.ItemService;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/item")
public class ItemController {

    private static Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}/login/{login}")
    public @ResponseBody
    ResponseEntity<Item> getItemByUserIdItemId(@PathVariable int id, @PathVariable String login) {
        log.debug("Try to get item with id '{}' for user with with login '{}'", id, login);
        Item item = itemService.findByUserIdItemId(id, login);

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

        Item addedItem = itemService.addItemToUserWishList(item);
        log.debug("Item was added with id '{}'", addedItem.getItemId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public @ResponseBody
    ResponseEntity deleteItemFromUserWishList(@PathVariable int id) {
        log.debug("Trying to delete item with id '{}' to user wish list", id);
        Item deletedItem = itemService.deleteItemFromUserWishList(id);

        log.debug("Send response status OK");
        return new ResponseEntity(HttpStatus.OK);
    }

// TODO: 08.05.2018 check like
    @PostMapping("/{id}/like")
    public @ResponseBody
    ResponseEntity<Item> addLike(@PathVariable int id) {
        log.debug("Trying to add like to item with id '{}'", id);

        Item likedItem = itemService.addLike(id);
        return new ResponseEntity<>(likedItem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like")
    public @ResponseBody
    ResponseEntity<Item> removeLike(@PathVariable int id) {
        log.debug("Trying to remove like from item with id '{}'", id);

        Item unlikedItem = itemService.removeLike(id);
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

    @DeleteMapping("/{itemId}/owner/{ownerId}/booker/{bookerId}")
    public ResponseEntity deleteItemBooker(@PathVariable int itemId, @PathVariable int ownerId, @PathVariable int bookerId) {
        log.debug("Trying to delete item with id '{}' to user wish list", itemId);

        Item itemWithoutBooker = itemService.deleteItemBooker(ownerId, itemId);

        log.debug("Booker with id '{}' was deleted from item '{}'", bookerId, itemWithoutBooker);
        return new ResponseEntity<>(itemWithoutBooker, HttpStatus.OK);
    }
}
