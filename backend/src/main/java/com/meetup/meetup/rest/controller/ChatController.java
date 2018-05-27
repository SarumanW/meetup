package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Message;
import com.meetup.meetup.service.ChatService;
import com.meetup.meetup.service.vm.ChatIdsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users/{userId}/chats")
@PropertySource("classpath:strings.properties")
public class ChatController {

    private static Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @PostMapping
    @PreAuthorize("@chatAuthorization.isUserOwnerOfEvent(#userId, #eventId)")
    public ResponseEntity<ChatIdsVM> addChats(@PathVariable int userId, @RequestBody int eventId) {
        log.debug("Trying to add chats for event with id '{}'", eventId);

        ChatIdsVM responseId = chatService.addChats(eventId);

        log.debug("Send response body chatId '{}' and status OK", responseId);

        return new ResponseEntity<>(responseId, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("@chatAuthorization.checkByEventId(#userId, #eventId)")
    public ResponseEntity<ChatIdsVM> getChatsIds(@PathVariable int userId, @PathVariable int eventId) {
        log.debug("Trying to get chats for event with id '{}'", eventId);

        ChatIdsVM responseId = chatService.getChatsIds(eventId);

        log.debug("Send response body chatId '{}' and status OK", responseId);

        return new ResponseEntity<>(responseId, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("@chatAuthorization.isUserOwnerOfEvent(#userId, #eventId)")
    public ResponseEntity<Integer> deleteChats(@PathVariable int userId, @PathVariable int eventId) {
        log.debug("Trying to delete eventId '{}'", eventId);

        chatService.deleteChats(eventId);

        log.debug("Send response body eventId '{}' and status OK", eventId);

        return new ResponseEntity<>(eventId, HttpStatus.OK);
    }

    @PostMapping("/message")
    @PreAuthorize("@chatAuthorization.checkByChatId(#userId, #message.chatId)")
    public ResponseEntity<Message> addMessage(@PathVariable int userId, @RequestBody Message message) {
        log.debug("Trying to add message for chat with id '{}'", message.getChatId());

        Message result = chatService.addMessage(message);

        log.debug("Send response body message '{}' and status CREATED", result.getMessageId());

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/messages/{chatId}")
    @PreAuthorize("@chatAuthorization.checkByChatId(#userId, #chatId)")
    public ResponseEntity<List<Message>> getMessages(@PathVariable int userId, @PathVariable int chatId) {
        log.debug("Trying to get messages for chat with id '{}'", chatId);

        List<Message> msgList = chatService.getMessagesByChatId(chatId);

        log.debug("Send response body list messages and status OK");

        return new ResponseEntity<>(msgList, HttpStatus.OK);
    }

    @GetMapping("{chatId}/members")
    @PreAuthorize("@chatAuthorization.checkByChatId(#userId, #chatId)")
    public ResponseEntity<List<String>> getMembers(@PathVariable int userId, @PathVariable int chatId) {
        log.debug("Trying to get members of chat with chatId '{}'", chatId);

        List<String> memberLogins = chatService.getUserLogins(chatId);

        log.debug("Received members '{}' from cache by chatId '{}'", chatId);

        return new ResponseEntity<>(memberLogins, HttpStatus.OK);
    }

    @PostMapping("{chatId}/member")
    @PreAuthorize("@chatAuthorization.checkByChatId(#userId, #chatId)")
    public ResponseEntity<String> addMember(@RequestBody String login, @PathVariable int userId,  @PathVariable int chatId) {
        log.debug("Trying to add member of chat with chatId '{}'", chatId);

        chatService.addUserLogin(login, chatId);

        log.debug("Added member '{}' from cache by chatId '{}'", login, chatId);

        return new ResponseEntity<>(login, HttpStatus.CREATED);
    }

    @DeleteMapping("{chatId}/member/{login}")
    @PreAuthorize("@chatAuthorization.checkByChatId(#userId, #chatId)")
    public ResponseEntity<Integer> deleteMember(@PathVariable String login, @PathVariable int userId, @PathVariable int chatId) {
        log.debug("Trying to delete member of chat with chatId '{}'", chatId);

        chatService.deleteUserLogin(login, chatId);

        log.debug("Deleted member '{}' from cache by chatId '{}'", login, chatId);

        return new ResponseEntity<>(userId, HttpStatus.OK);
    }
}
