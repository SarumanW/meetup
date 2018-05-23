package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.ItemComment;
import com.meetup.meetup.service.ItemCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/comment")
public class ItemCommentController {
    private static Logger log = LoggerFactory.getLogger(ItemCommentController.class);

    private final ItemCommentService itemCommentService;

    @Autowired
    public ItemCommentController(ItemCommentService itemCommentService) {
        this.itemCommentService = itemCommentService;
    }

    @GetMapping("{id}")
    public ItemComment getItemCommentById(@PathVariable int id){
        log.debug("Try to get comment item with id '{}'", id);
        ItemComment itemComment = itemCommentService.findById(id);

        log.debug("Send response body item comment '{}' and status OK", itemComment);
        return itemComment;
    }

    @PreAuthorize("@itemCommentPermissionChecker.canDeleteItemComment(#id)")
    @DeleteMapping("{id}")
    public ItemComment deleteById(@PathVariable int id){
        log.debug("Try to delete comment item with id '{}'", id);
        ItemComment deletedItem = itemCommentService.deleteById(id);

        log.debug("Send response body item comment '{}' and status OK", deletedItem);
        return deletedItem;
    }

    @PostMapping("{itemId}")
    public ItemComment insert(@Valid @RequestBody String itemComment, @PathVariable int itemId){
        log.debug("Try to insert comment '{}'", itemComment);
        ItemComment addedComment = itemCommentService.insert(itemComment, itemId);

        log.debug("Send response body saved item comment '{}' and status CREATED", addedComment);
        return addedComment;
    }

    @GetMapping("{itemId}/comments")
    public List<ItemComment> getCommentsByItemId(@PathVariable int itemId){
        log.debug("Try to get comments for item with id '{}'", itemId);
        List<ItemComment> comments = itemCommentService.getCommentsByItemId(itemId);

        log.debug("Send response body comments '{}' and status CREATED", comments);
        return comments;
    }
}
