package com.meetup.meetup.rest.controller;

import com.meetup.meetup.service.vm.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/send/message/{chatId}")
    @SendTo("/chat/{chatId}")
    public ChatMessage onReceivedMessage(@Payload ChatMessage message,
                                         @DestinationVariable int chatId){
        return message;
    }

    @MessageMapping("/add/{chatId}")
    @SendTo("/chat/{chatId}")
    public ChatMessage addUser(@Payload ChatMessage message,
                                         SimpMessageHeaderAccessor headerAccessor,
                               @DestinationVariable int chatId){
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }
}
