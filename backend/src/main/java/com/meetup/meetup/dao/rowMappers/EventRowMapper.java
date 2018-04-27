package com.meetup.meetup.dao.rowMappers;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.EventPeriodicity;
import com.meetup.meetup.entity.EventType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int i) throws SQLException {
        Event event = new Event();
        Timestamp date;

        event.setEventId(resultSet.getInt("EVENT_ID"));
        event.setName(resultSet.getString("NAME"));
        date = resultSet.getTimestamp("EVENT_DATE");
        event.setEventDate(date == null ? null : date.toString());
        event.setDescription(resultSet.getString("DESCRIPTION"));
        event.setPeriodicityId(resultSet.getInt("PERIODICITY_ID"));
        event.setPeriodicity(EventPeriodicity.valueOf(resultSet.getString("PERIODICITY_NAME")));
        event.setPlace(resultSet.getString("PLACE"));
        event.setEventTypeId(resultSet.getInt("EVENT_TYPE_ID"));
        event.setEventType(EventType.valueOf(resultSet.getString("TYPE")));
        event.setDraft(resultSet.getInt("IS_DRAFT") == 1);
        event.setFolderId(resultSet.getInt("FOLDER_ID"));
        event.setImageFilepath(resultSet.getString("IMAGE_FILEPATH"));
        event.setOwnerId(resultSet.getInt("OWNER_ID"));

        return event;
    }
}
