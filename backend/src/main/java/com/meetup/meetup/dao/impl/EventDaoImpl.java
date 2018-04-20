package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.rowMappers.EventRowMapper;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.rest.controller.errors.DatabaseWorkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@PropertySource("classpath:sqlDao.properties")
public class EventDaoImpl implements EventDao {

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    @Override
    public Event findById(int id) {
        Event event = new Event();

        try {
            event = jdbcTemplate.queryForObject(
                    env.getProperty("event.findById"),
                    new Object[]{id}, new EventRowMapper()
            );
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return event;
    }

    @Override
    public Event insert(Event model) {
        int id;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("EVENT")
                .usingGeneratedKeyColumns("EVENT_ID");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("EVENT_ID", model.getEventId());
        parameters.put("NAME", model.getName());
        parameters.put("EVENT_DATE",  model.getEventDate());
        parameters.put("DESCRIPTION", model.getDescription());
        parameters.put("PERIODICITY_ID", model.getPeriodicityId());
        parameters.put("PLACE", model.getPlace());
        parameters.put("EVENT_TYPE_ID", model.getEventTypeId());
        parameters.put("IS_DRAFT", model.isDraft() ? 1 : 0);
        parameters.put("FOLDER_ID", model.getFolderId());
        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setEventId(id);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        return model;
    }

    @Override
    public Event update(Event model) {
        try {
            jdbcTemplate.update(env.getProperty("event.update"),
                    model.getName(), model.getEventDate(), model.getDescription(), model.getPeriodicityId(),
                    model.getPlace(), model.getEventTypeId(), model.isDraft() ? 1 : 0, model.getFolderId(), model.getEventId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        return model;
    }

    @Override
    public Event delete(Event model) {
        try {
            jdbcTemplate.update(env.getProperty("event.delete"), model.getEventId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        return model;
    }


    @Override
    public List<User> getParticipants(Event event) {

        throw new NotImplementedException();

    }
}
