package com.meetup.meetup.service;

import com.meetup.meetup.service.vm.ChatIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:strings.properties")
public class ChatService {

    private static Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private Environment env;

    public ChatIds addChats(int eventId) {
        return new ChatIds(eventId - 1, eventId + 1);
    }

    public ChatIds getChatsIds(int eventId) {
        return new ChatIds(eventId - 1, eventId + 1);
    }
}
