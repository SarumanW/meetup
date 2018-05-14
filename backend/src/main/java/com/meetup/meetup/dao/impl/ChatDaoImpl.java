package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.ChatDao;
import com.meetup.meetup.dao.rowMappers.MessageRowMapper;
import com.meetup.meetup.entity.Message;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meetup.meetup.keys.Key.*;

public class ChatDaoImpl implements ChatDao {

    @Autowired
    private Environment env;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static Logger log = LoggerFactory.getLogger(ChatDaoImpl.class);
    private static final int EVENT_CHATS_COUNT = 2;
    private static final int CHAT_ID_WITH_OWNER = 1;
    private static final int CHAT_ID_WITHOUT_OWNER = 2;

    @Override
    public Message insertMessage(Message message) {
        int id;
        log.debug("Try to insert message '{}'", message);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_MESSAGE)
                .usingGeneratedKeyColumns(MESSAGE_MESSAGE_ID);

        try {
            id = simpleJdbcInsert.executeAndReturnKey(MessageRowMapper.paramsMapper(message)).intValue();
            message.setMessageId(id);
        } catch (DataAccessException e) {
            log.error("Query fails by insert message '{}'", message);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Message '{}' was insert" + (id != 0 ? "ed with id '{}'" : "ing failed"), message, id);

        return message;
    }

    @Override
    @Transactional
    public List<Integer> createChatsByEventId(int eventId) {
        int chatIdWithOwner, chatIdWithoutOwner;

        log.debug("Try to insert chat with owner with eventId '{}'", eventId);

        chatIdWithOwner = insertChat(eventId, CHAT_ID_WITH_OWNER);

        log.debug("Chat with owner was successfully inserted with id '{}'", chatIdWithOwner);

        log.debug("Try to insert chat without owner with eventId '{}'", eventId);

        chatIdWithoutOwner = insertChat(eventId, CHAT_ID_WITHOUT_OWNER);

        log.debug("Chat without owner was successfully inserted with id '{}'", chatIdWithoutOwner);

        List<Integer> eventChats = new ArrayList<>(EVENT_CHATS_COUNT);

        eventChats.add(chatIdWithOwner);
        eventChats.add(chatIdWithoutOwner);

        return eventChats;
    }

    private int insertChat(int eventId, int chatTypeId) {
        int chatId;

        log.debug("Try to insert chat by eventId '{}'", eventId);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_CHAT)
                .usingGeneratedKeyColumns(CHAT_CHAT_ID);

        Map<String, Object> chat = new HashMap<>();
        chat.put(CHAT_CHAT_TYPE_ID, chatTypeId);
        chat.put(CHAT_EVENT_ID, eventId);

        try {
            chatId = simpleJdbcInsert.executeAndReturnKey(chat).intValue();
        } catch (DataAccessException e) {
            log.error("Query fails by insert chat '{}'", chat);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Chat was insert" + (chatId == 0 ? "ed with id '{}'" : "ing failed"), chatId);

        return chatId;
    }

    @Override
    @Transactional
    public boolean deleteChatsByEventId(int eventId) {
        log.debug("Try to delete chats by event id '{}'", eventId);

        int result;

        deleteMessagesByEventId(eventId);

        try {
            result = jdbcTemplate.update(env.getProperty(CHAT_DELETE_BY_EVENT_ID));
        } catch (DataAccessException e) {
            log.error("Query fails by delete event chats with eventId '{}'", eventId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (result < 2) {
            log.debug("Chats deleting failed with eventId '{}'", eventId);
        } else {
            log.debug("Chats was deleted successfully by eventId '{}'", eventId);
        }

        return result >= 2;
    }

    private void deleteMessagesByEventId(int eventId) {
        log.debug("Try to delete messages by event id '{}'", eventId);

        int result;

        try {
            result = jdbcTemplate.update(env.getProperty(CHAT_DELETE_MESSAGES_BY_EVENT_ID));
        } catch (DataAccessException e) {
            log.error("Query fails by delete event chats messages with eventId '{}'", eventId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Messages was deleted successfully by eventId '{}', count = '{}'", eventId, result);
    }

    @Override
    public List<Integer> findChatsIdsByEventId(int eventId) {
        List<Integer> eventChats = new ArrayList<>();

        log.debug("Try to find events with folder id '{}'", eventId);

        try {
            eventChats = jdbcTemplate.queryForList(env.getProperty(CHAT_FIND_CHATS_IDS_BY_EVENT_ID),
                    new Object[]{eventId}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Chats ids not found by eventId: '{}'", eventId);
            return eventChats;
        } catch (DataAccessException e) {
            log.error("Query fails by finding chats ids with eventId '{}'", eventId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Chats '{}' was found by eventId '{}'", eventChats, eventId);

        return eventChats;
    }

    @Override
    public List<Message> findMessagesByChatId(int chatId) {
        List<Message> messages = new ArrayList<>();

        log.debug("Try to find messages with chatId '{}'", chatId);

        try {
            messages = jdbcTemplate.query(env.getProperty(CHAT_FIND_MESSAGES_BY_CHAT_ID),
                    new Object[]{chatId}, new MessageRowMapper());
        } catch (EmptyResultDataAccessException e) {
            log.debug("Messages not found by chatId '{}'", chatId);
            return messages;
        } catch (DataAccessException e) {
            log.error("Query fails by finding messages with chatId '{}'", chatId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Chat messages '{}' was found by chatId '{}'", messages, chatId);

        return messages;
    }
}
