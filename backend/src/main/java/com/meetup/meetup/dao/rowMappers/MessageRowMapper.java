package com.meetup.meetup.dao.rowMappers;

import com.meetup.meetup.entity.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.meetup.meetup.keys.Key.*;

public class MessageRowMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }

    public static Map<String, Object> paramsMapper(Message message) {
        Map<String, Object> paramsMapper = new HashMap<>();
        paramsMapper.put(MESSAGE_MESSAGE_ID, message.getMessageId());
        paramsMapper.put(MESSAGE_SENDER_ID, message.getSenderId());
        paramsMapper.put(MESSAGE_CHAT_ID, message.getChatId());
        paramsMapper.put(MESSAGE_TEXT, message.getText());
        paramsMapper.put(MESSAGE_MESSAGE_DATE, (message.getMessageDate() != null ? Timestamp.valueOf(message.getMessageDate()) : null));
        return paramsMapper;
    }
}
