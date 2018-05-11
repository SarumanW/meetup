package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.service.ChatService;
import com.meetup.meetup.service.vm.ChatIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/chats")
@PropertySource("classpath:strings.properties")
public class ChatController {

    private static Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private Environment env;

    @Autowired
    private ChatService chatService;

    @PostMapping("/add")
    public ResponseEntity<ChatIds> addChats(@RequestBody int eventId) {
        log.debug("Trying to add chats for event with id '{}'", eventId);

        ChatIds responseId = chatService.addChats(eventId);

        log.debug("Send response body chatId '{}' and status OK", responseId);

        return new ResponseEntity<>(responseId, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ChatIds> getChatsIds(@PathVariable int eventId) {
        log.debug("Trying to get chats for event with id '{}'", eventId);

        ChatIds responseId = chatService.getChatsIds(eventId);

        log.debug("Send response body chatId '{}' and status OK", responseId);

        return new ResponseEntity<>(responseId, HttpStatus.CREATED);
    }
}
