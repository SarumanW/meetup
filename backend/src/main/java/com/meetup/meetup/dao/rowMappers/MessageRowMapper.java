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
    public Message mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Message message = new Message();

        message.setMessageId(resultSet.getInt(MESSAGE_MESSAGE_ID));
        message.setSenderId(resultSet.getInt(MESSAGE_SENDER_ID));
        message.setChatId(resultSet.getInt(MESSAGE_CHAT_ID));
        message.setText(resultSet.getString(MESSAGE_TEXT));
        message.setSenderLogin(resultSet.getString(MESSAGE_SENDER_LOGIN));
        Timestamp date = resultSet.getTimestamp(MESSAGE_MESSAGE_DATE);
        message.setMessageDate(date == null ? null : date.toString());

        return message;
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
