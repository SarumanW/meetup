package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.ChatDao;
import com.meetup.meetup.dao.rowMappers.MessageRowMapper;
import com.meetup.meetup.entity.Message;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import com.meetup.meetup.service.vm.ChatCheckEntity;
import com.meetup.meetup.service.vm.ChatIdsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meetup.meetup.keys.Key.*;

@Repository
public class ChatDaoImpl implements ChatDao {

    @Autowired
    private Environment env;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static Logger log = LoggerFactory.getLogger(ChatDaoImpl.class);
    private static final int CHAT_ID_WITH_OWNER = 1;
    private static final int CHAT_ID_WITHOUT_OWNER = 2;
    private static final int GET_MESSAGE_LIMIT = 50;

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
            log.error("Query fails by insert message '{}'", message, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Message '{}' was inserted with id '{}'", message, id);
        return message;
    }

    @Override
    @Transactional
    public ChatIdsVM createChatsByEventId(int eventId) {
        ChatIdsVM chatIdsVM = new ChatIdsVM();

        log.debug("Try to insert chat with owner with eventId '{}'", eventId);

        chatIdsVM.setPublicChatId(insertChat(eventId, CHAT_ID_WITH_OWNER));

        log.debug("Chat with owner was successfully inserted with id '{}'", chatIdsVM.getPublicChatId());

        log.debug("Try to insert chat without owner with eventId '{}'", eventId);

        chatIdsVM.setPrivateChatId(insertChat(eventId, CHAT_ID_WITHOUT_OWNER));

        log.debug("Chat without owner was successfully inserted with id '{}'", chatIdsVM.getPrivateChatId());

        return chatIdsVM;
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
            log.error("Query fails by insert chat '{}'", chat, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Chat was inserted with id '{}'", chatId);
        return chatId;
    }

    @Override
    public void deleteChatsByEventId(int eventId) {
        log.debug("Try to delete chats by event id '{}'", eventId);

        int result;

        deleteMessagesByEventId(eventId);

        try {
            result = jdbcTemplate.update(env.getProperty(CHAT_DELETE_BY_EVENT_ID), eventId);
        } catch (DataAccessException e) {
            log.error("Query fails by delete event chats with eventId '{}'", eventId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (result < 2) {
            log.error("Chats deleting failed with eventId '{}'", eventId);
        } else {
            log.debug("Chats was deleted successfully by eventId '{}'", eventId);
        }
    }

    private void deleteMessagesByEventId(int eventId) {
        log.debug("Try to delete messages by event id '{}'", eventId);

        int result;

        try {
            result = jdbcTemplate.update(env.getProperty(CHAT_DELETE_MESSAGES_BY_EVENT_ID), eventId);
        } catch (DataAccessException e) {
            log.error("Query fails by delete event chats messages with eventId '{}'", eventId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Messages was deleted successfully by eventId '{}', count = '{}'", eventId, result);
    }

    @Override
    public ChatIdsVM findChatsIdsByEventId(int eventId) {
        ChatIdsVM chatIdsVM = new ChatIdsVM();

        log.debug("Try to find chat with owner with eventId '{}'", eventId);

        chatIdsVM.setPublicChatId(findChatIdByEventIdAndChatTypeId(eventId, CHAT_ID_WITH_OWNER));

        log.debug("Chat with owner was successfully found with id '{}'", chatIdsVM.getPublicChatId());

        log.debug("Try to find chat without owner with eventId '{}'", eventId);

        chatIdsVM.setPrivateChatId(findChatIdByEventIdAndChatTypeId(eventId, CHAT_ID_WITHOUT_OWNER));

        log.debug("Chat without owner was successfully found with id '{}'", chatIdsVM.getPrivateChatId());

        return chatIdsVM;
    }

    private int findChatIdByEventIdAndChatTypeId(int eventId, int chatTypeId) {
        int chatId;

        log.debug("Try to find events with eventId '{}' and chatTypeId '{}'", eventId);

        try {
            List<Integer> chatIds = jdbcTemplate.query(env.getProperty(CHAT_FIND_CHAT_ID_BY_EVENT_ID_AND_CHAT_TYPE_ID),
                    new Object[]{eventId, chatTypeId}, (rs,rowNum)->rs.getInt(1));
            if(chatIds.isEmpty()){
                throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "Chat", "eventId", eventId));
            } else{
                chatId = chatIds.get(0);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by finding chats ids with eventId '{}' and chatTypeId '{}'", eventId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Chat '{}' was found by eventId '{}' and chatTypeId '{}'", chatId, eventId);

        return chatId;
    }

    @Override
    public List<Message> findMessagesByChatId(int chatId) {
        List<Message> messages;

        log.debug("Try to find messages with chatId '{}'", chatId);

        try {
            messages = jdbcTemplate.query(env.getProperty(CHAT_FIND_MESSAGES_BY_CHAT_ID),
                    new Object[]{chatId, GET_MESSAGE_LIMIT}, new MessageRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by finding messages with chatId '{}'", chatId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Chat messages '{}' was found by chatId '{}'", messages, chatId);

        return messages;
    }

    @Override
    public ChatCheckEntity canJoinChat(int userId, int chatId) {

        ChatCheckEntity checkEntity = new ChatCheckEntity();
        final String[] role = new String[1];

        log.debug("Try to find permissions with chatId '{}' and userId '{}'", chatId, userId);

        try {
            jdbcTemplate.query(env.getProperty(CHAT_CAN_JOIN_CHAT), new Object[]{chatId, userId, chatId},
                    (resultSet, i) -> {
                        checkEntity.setChatTypeId(resultSet.getInt(CHAT_TYPE_ID));
                        role[0] = resultSet.getString(ROLE_NAME);
                        checkEntity.setRole( role[0] == null ? Role.NULL : Role.valueOf(role[0]));
                        return checkEntity;
                    });
        } catch (DataAccessException e) {
            log.error("Query fails by finding messages with chatId '{}' and userId '{}'", chatId, userId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        log.debug("User with id '{}' can join to chat with id '{}' : '{}'", userId, chatId, checkEntity);
        return checkEntity;

    }
}
