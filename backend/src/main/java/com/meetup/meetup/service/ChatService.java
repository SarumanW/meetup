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

    public List<Message> getMessagesByChatId(int chatId){
        return chatDao.findMessagesByChatId(chatId);
    }

    public Message addMessage(Message message){
        return chatDao.insertMessage(message);
    }

    public ChatIdsVM addChats(int eventId) {
        return chatDao.createChatsByEventId(eventId);
    }

    public ChatIdsVM getChatsIds(int eventId) {
        return chatDao.findChatsIdsByEventId(eventId);
    }

    public void deleteChats(int eventId) {
        chatDao.deleteChatsByEventId(eventId);
    }

    public void addUserLogin(String login, int chatId) {
        log.debug("Trying to add member of chat with chatId '{}' and login '{}'", chatId, login);
        if (!userLogins.containsKey(chatId)) {
            userLogins.put(chatId, new ArrayList<>());
        }

        userLogins.get(chatId).add(login);
    }

    public List<String> getUserLogins(int chatId) {
        return userLogins.get(chatId);
    }

    public void deleteUserLogin(String login, int chatId) {
        log.debug("Trying to delete member of chat with chatId '{}' and login '{}'", chatId, login);

        boolean deleted = false;

        if (userLogins.containsKey(chatId)) {
            deleted = userLogins.get(chatId).remove(login);
        }

        if (!deleted) {
            throw new DeleteException(EXCEPTION_DELETE);
        }

        log.debug("Delete member with login '{}'", login);
    }
}
