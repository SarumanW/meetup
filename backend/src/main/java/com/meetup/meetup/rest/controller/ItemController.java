package com.meetup.meetup.rest.controller;


import com.meetup.meetup.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.meetup.meetup.service.ItemService;

@RestController
@RequestMapping(path = "/api/item")
public class ItemController {

    private static Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable int id){
        log.debug("Try to get item with id '{}'", id);
        Item item = itemService.getItemById(id);

        log.debug("Send response body item '{}' and status OK", item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
}
