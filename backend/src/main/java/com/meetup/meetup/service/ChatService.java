package com.meetup.meetup.service;

import com.meetup.meetup.dao.ChatDao;
import com.meetup.meetup.entity.Message;
import com.meetup.meetup.exception.runtime.DeleteException;
import com.meetup.meetup.service.vm.ChatIdsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meetup.meetup.keys.Key.EXCEPTION_DELETE;
import static com.meetup.meetup.keys.Key.EXCEPTION_UPDATE;

@Service
@PropertySource("classpath:strings.properties")
public class ChatService {

    private static Logger log = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private Environment env;

    @Autowired
    private ChatDao chatDao;

    private Map<Integer, List<String>> userLogins = new HashMap<>();

    public ChatIdsVM addChats(int eventId) {
        log.debug("Trying to add chats for event with id '{}'", eventId);

        ChatIdsVM chatIdsVM = chatDao.createChatsByEventId(eventId);

        log.debug("Created chats '{}' by eventId '{}'", chatIdsVM, eventId);

        return chatIdsVM;
    }

    public ChatIdsVM getChatsIds(int eventId) {
        log.debug("Trying to get chats for event with id '{}'", eventId);

        ChatIdsVM chatIdsVM = chatDao.findChatsIdsByEventId(eventId);

        log.debug("Received chats '{}' by eventId '{}'", chatIdsVM, eventId);

        return chatIdsVM;
    }

    public void deleteChats(int eventId) {
        log.debug("Trying to delete chats for event with id '{}'", eventId);

        chatDao.deleteChatsByEventId(eventId);
    }

    public Message addMessage(Message message){
        log.debug("Trying to add message '{}'", message);

        Message addedMessage = chatDao.insertMessage(message);

        log.debug("Message '{}' added successful", addedMessage);

        return addedMessage;
    }

    public List<Message> getMessagesByChatId(int chatId){
        log.debug("Trying to get messages by chat id '{}'", chatId);

        List<Message> messages = chatDao.findMessagesByChatId(chatId);

        log.debug("Messages was received");

        return messages;
    }

    public void addUserLogin(String login, int chatId) {
        log.debug("Trying to add member of chat with chatId '{}' and login '{}'", chatId, login);

        if (!userLogins.containsKey(chatId)) {
            userLogins.put(chatId, new ArrayList<>());
        }

        userLogins.get(chatId).add(login);

        log.debug("Member was added to chat");
    }

    public List<String> getUserLogins(int chatId) {
        log.debug("Trying to get members of chat '{}'", chatId);

        List<String> members = userLogins.get(chatId);

        log.debug("Received members '{}'", members);

        return members;
    }

    public void deleteUserLogin(String login, int chatId) {
        log.debug("Trying to delete member of chat with chatId '{}' and login '{}'", chatId, login);

        boolean deleted = false;

        if (userLogins.containsKey(chatId)) {
            deleted = userLogins.get(chatId).remove(login);
        }

        if (!deleted) {
            log.error("Failed deleting member with login '{}' from chat '{}'", login, chatId);
            throw new DeleteException(EXCEPTION_DELETE);
        }

        log.debug("Member was deleted with login '{}' from chat '{}'", login, chatId);
    }
}
