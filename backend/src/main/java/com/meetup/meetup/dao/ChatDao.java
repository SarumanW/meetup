package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Message;

import java.util.List;

public interface ChatDao {

    //Chats

    // TODO: 14.05.2018 CHECK WORKING
    List<Integer> createChatsByEventId(int eventId);

    // TODO: 14.05.2018 CHECK WORKING
    boolean deleteChatsByEventId(int eventId);

    // TODO: 14.05.2018 CHECK WORKING
    List<Integer> findChatsIdsByEventId(int eventId);

    //Messages

    // TODO: 14.05.2018 CHECK WORKING
    Message insertMessage(Message message);

    // TODO: 14.05.2018 CHECK WORKING
    List<Message> findMessagesByChatId(int chatId);
}
