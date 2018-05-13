package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Message;

import java.util.List;

public interface ChatDao {
    Message insertMessage(Message message);

    boolean createChatsByEventId(int eventId);

    boolean deleteChatsByEventId(int eventId);

    List<Integer> findChatsIdsByEventId();

    List<Message> findMessagesByEventIdAndChatType();
}
