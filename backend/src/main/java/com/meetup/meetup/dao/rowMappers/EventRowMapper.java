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
        switch (resultSet.getString("PERIODICITY_NAME")) {
            case "ONCE":
                event.setPeriodicity(EventPeriodicity.ONCE);
                break;
            case "HOUR":
                event.setPeriodicity(EventPeriodicity.HOUR);
                break;
            case "DAY":
                event.setPeriodicity(EventPeriodicity.DAY);
                break;
            case "WEEK":
                event.setPeriodicity(EventPeriodicity.WEEK);
                break;
            case "MONTH":
                event.setPeriodicity(EventPeriodicity.MONTH);
                break;
            case "YEAR":
                event.setPeriodicity(EventPeriodicity.YEAR);
                break;
        }
        event.setPlace(resultSet.getString("PLACE"));
        event.setEventTypeId(resultSet.getInt("EVENT_TYPE_ID"));
        event.setEventType("EVENT".equals(resultSet.getString("TYPE")) ? EventType.EVENT : EventType.NOTE);
        event.setDraft(resultSet.getInt("IS_DRAFT") == 1);
        event.setFolderId(resultSet.getInt("FOLDER_ID"));

        return event;
    }
}
