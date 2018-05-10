package com.meetup.meetup.rest.controller;

import com.meetup.meetup.service.vm.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @Autowired
    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/send/message/{eventId}")
    @SendTo("/chat/{eventId}")
    public ChatMessage onRecievedMessage(@Payload ChatMessage message,
                                         @DestinationVariable int eventId){
//        this.template.convertAndSend("/chat",
//                new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - " + message);
        return message;
    }

    @MessageMapping("/add/{eventId}")
    @SendTo("/chat/{eventId}")
    public ChatMessage addUser(@Payload ChatMessage message,
                                         SimpMessageHeaderAccessor headerAccessor,
                               @DestinationVariable int eventId){
//        this.template.convertAndSend("/chat",
//                new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - " + message);
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }
}
