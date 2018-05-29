package com.meetup.meetup.rest.controller;


import com.meetup.meetup.entity.Item;

import com.meetup.meetup.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.meetup.meetup.service.ItemService;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/users/{userId}/items")
public class ItemController {

    private static Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;
    private final StorageService storageService;

    @Autowired
    public ItemController(ItemService itemService, StorageService storageService) {
        this.itemService = itemService;
        this.storageService = storageService;
    }

    @GetMapping("/{itemId}/login/{login}")
    public ResponseEntity<Item> getItemByUserLoginAndItemId(@PathVariable int userId, @PathVariable int itemId, @PathVariable String login) {
        log.debug("Try to get item with id '{}' for user with with login '{}'", itemId, login);

        Item item = itemService.findByUserIdItemId(itemId, login);

        log.debug("Send response body item '{}' and status OK", item);

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("@itemAuthorization.isCorrectItem(#userId, #item)")
    public ResponseEntity<Item> addItem(@PathVariable int userId, @Valid @RequestBody Item item) {
        log.debug("Trying to save item {}", item);

        Item addedItem = itemService.addItem(item);

        log.debug("Send response body saved item '{}' and status CREATED", addedItem);

        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @PostMapping("/{itemId}")
    @PreAuthorize("@itemAuthorization.isCorrectItem(#userId, #itemId, #item)")
    public ResponseEntity<Item> addItemToUserWishList(@PathVariable int userId, @PathVariable int itemId, @Valid @RequestBody Item item){
        log.debug("Trying to add item with id '{}' to user wish list", item.getItemId());

        Item addedItem = itemService.addItemToUserWishList(userId, item);

        log.info("Added item with id '{}' to user wish list", addedItem.getItemId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("@itemAuthorization.isCorrectItem(#userId, #itemId, #newItem)")
    public ResponseEntity<Item> updateItem(@PathVariable int userId, @PathVariable int itemId, @Valid @RequestBody Item newItem) {
        log.debug("Trying to update item '{}'", newItem);

        Item updatedItem = itemService.updateItem(newItem);

        log.debug("Send response body updated '{}' and status OK");

        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("@itemAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Item> deleteItem(@PathVariable int userId, @PathVariable int itemId) {
        log.debug("Trying to delete item with id '{}' to user wish list", itemId);

        Item deletedItem = itemService.deleteItemFromUserWishList(userId, itemId);

        log.debug("Send response status OK");

        return new ResponseEntity<>(deletedItem, HttpStatus.OK);
    }

    @PostMapping("/{itemId}/owner/{ownerId}")
    @PreAuthorize("@itemAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Item> addItemBooker(@PathVariable int itemId, @PathVariable int ownerId, @PathVariable int userId){
        log.debug("Trying to add item with id '{}' to user wish list", itemId);

        Item itemWithBooker = itemService.addItemBooker(ownerId, itemId, userId);

        log.debug("Booker with id '{}' was added to item '{}'", userId, itemWithBooker);

        return new ResponseEntity<>(itemWithBooker, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}/owner/{ownerId}")
    @PreAuthorize("@itemAuthorization.isUserCorrect(#userId)")
    public ResponseEntity deleteItemBooker(@PathVariable int itemId, @PathVariable int ownerId, @PathVariable int userId) {
        log.debug("Trying to delete item with id '{}' to user wish list", itemId);

        Item itemWithoutBooker = itemService.deleteItemBooker(ownerId, itemId, userId);

        log.debug("Booker with id '{}' was deleted from item '{}'", userId, itemWithoutBooker);

        return new ResponseEntity<>(itemWithoutBooker, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("@itemAuthorization.isCorrectItem(#userId, #item)")
    public ResponseEntity deleteItem(@PathVariable int userId, @Valid @RequestBody Item item) {
        log.debug("Trying to delete item '{}'", item);

        Item deletedItem = itemService.deleteItem(item);

        log.debug("Send response status OK");

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{itemId}/like")
    @PreAuthorize("@itemAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Item> addLike(@PathVariable int userId, @PathVariable int itemId) {
        log.debug("Trying to add like to item with id '{}'", itemId);

        Item likedItem = itemService.addLike(userId, itemId);

        return new ResponseEntity<>(likedItem, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}/like")
    @PreAuthorize("@itemAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Item> removeLike(@PathVariable int userId, @PathVariable int itemId) {
        log.debug("Trying to remove like from item with id '{}'", itemId);

        Item unlikedItem = itemService.removeLike(userId, itemId);

        return new ResponseEntity<>(unlikedItem, HttpStatus.OK);
    }

    @GetMapping("/{itemId}/likes")
    @PreAuthorize("@itemAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<String>> getUserLoginsWhoLikedItem(@PathVariable int userId, @PathVariable int itemId) {
        log.debug("Try to get login who liked item with idItem '{}'", itemId);

        List<String> logins = itemService.getUserLoginsWhoLikedItem(itemId);

        log.debug("Send response body login who liked item '{}' and status OK", logins);

        return new ResponseEntity<>(logins, HttpStatus.OK);
    }

    @PostMapping("/upload")
    @PreAuthorize("@itemAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<String> handleFileUpload(@PathVariable int userId, @RequestParam MultipartFile file) {
        log.debug("Trying to upload image '{}'", file);

        String imagePath = storageService.wishItemImageStore(file);

        log.debug("Image successfully uploaded send response status OK");

        return new ResponseEntity<>(imagePath, HttpStatus.OK);
    }

}
