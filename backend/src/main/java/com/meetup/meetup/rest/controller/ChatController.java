package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Message;
import com.meetup.meetup.service.ChatService;
import com.meetup.meetup.service.vm.ChatIdsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/chats")
@PropertySource("classpath:strings.properties")
public class ChatController {

    private static Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private Environment env;

    @Autowired
    private ChatService chatService;

    @PreAuthorize("@eventPermissionChecker.checkById(#eventId)")
    @PostMapping("/add")
    public ResponseEntity<ChatIdsVM> addChats(@RequestBody int eventId) {
        log.debug("Trying to add chats for event with id '{}'", eventId);

        ChatIdsVM responseId = chatService.addChats(eventId);

        log.debug("Send response body chatId '{}' and status OK", responseId);

        return new ResponseEntity<>(responseId, HttpStatus.CREATED);
    }

    @PreAuthorize("@chatPermissionChecker.checkByEventId(#eventId)")
    @GetMapping("/{eventId}")
    public ResponseEntity<ChatIdsVM> getChatsIds(@PathVariable int eventId) {
        log.debug("Trying to get chats for event with id '{}'", eventId);

        ChatIdsVM responseId = chatService.getChatsIds(eventId);

        log.debug("Send response body chatId '{}' and status OK", responseId);

        return new ResponseEntity<>(responseId, HttpStatus.CREATED);
    }

    @PreAuthorize("@eventPermissionChecker.checkById(#eventId)")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Integer> deleteChats(@PathVariable int eventId) {
        log.debug("Trying to delete eventId '{}'", eventId);

        chatService.deleteChats(eventId);

        log.debug("Send response body eventId '{}' and status OK", eventId);

        return new ResponseEntity<>(eventId, HttpStatus.OK);
    }

    @PreAuthorize("@chatPermissionChecker.checkById(#message.chatId)")
    @PostMapping("/message")
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {
        log.debug("Trying to add message for chat with id '{}'", message.getChatId());

        Message result = chatService.addMessage(message);

        log.debug("Send response body messageId '{}' and status OK", result.getMessageId());

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PreAuthorize("@chatPermissionChecker.checkById(#chatId)")
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable int chatId) {
        log.debug("Trying to get messages for chat with id '{}'", chatId);

        List<Message> msgList = chatService.getMessagesByChatId(chatId);

        return new ResponseEntity<>(msgList, HttpStatus.CREATED);
    }
}
