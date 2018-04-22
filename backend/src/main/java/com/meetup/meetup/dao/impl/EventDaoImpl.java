package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.rowMappers.EventRowMapper;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.DatabaseWorkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    private final int ownerId = 1;


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

    public Event createEvent(Event model, int userId) {

        Event event;

        event = insert(model);
        insertUserEvent(userId, model.getEventId(), ownerId);

        return event;
    }

    public Role getRole(int userId, int eventId) {

        Role role = null;

        try {
            String roleString = jdbcTemplate.queryForObject(
                    env.getProperty("role.getRole"), new Object[] { userId, eventId },
                    String.class);

            role = "OWNER".equals(roleString) ? Role.OWNER : Role.PARTICIPANT;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return role;
    }

    private int insertUserEvent(int userId, int eventId, int roleId) {


        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("USER_EVENT");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("USER_ID", userId);
        parameters.put("EVENT_ID", eventId);
        parameters.put("ROLE_ID", roleId);

        try {
            simpleJdbcInsert.execute(parameters);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }

        return roleId;
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
    public List<Event> findByFolderId(int folderId) {
        List<Event> events = new ArrayList<>();

        try {
            events = jdbcTemplate.query(env.getProperty("event.findByFolderId"),
                    new Object[]{folderId}, new EventRowMapper());
            System.out.println(events);
        } catch (DataAccessException e){
            System.out.println(e.getMessage());
        }

        return events;
    }

    @Override
    public List<User> getParticipants(Event event) {

        throw new NotImplementedException();

    }
}