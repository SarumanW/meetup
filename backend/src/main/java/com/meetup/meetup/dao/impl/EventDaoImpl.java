package com.meetup.meetup.dao.impl;

import com.meetup.meetup.Keys.Key;
import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.rowMappers.EventRowMapper;
import com.meetup.meetup.dao.rowMappers.UserRowMapper;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.EventType;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.DatabaseWorkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import static com.meetup.meetup.Keys.Key.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@PropertySource("classpath:sqlDao.properties")
@PropertySource("classpath:image.properties")
public class EventDaoImpl implements EventDao {

    private static Logger log = LoggerFactory.getLogger(EventDaoImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private UserDao userDao;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private final int ownerId = 1;
    private final int participantId = 2;


    @Override
    public List<Event> findByUserId(int userId) {
        List<Event> events;
        log.debug("Try to find list of events by user with id '{}'", userId);
        try {
            events = jdbcTemplate.query(EVENT_FIND_BY_USER_ID,
                    new Object[]{userId}, new EventRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by finding event by user with id '{}'", userId);
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (events.isEmpty()) {
            log.debug("Events for user with id '{}' not found", userId);
        } else {
            log.debug("Events for user with id '{}' counted '{}'", userId, events.size());
        }

        return events;
    }

    @Override
    public Event findById(int id) {
        Event event = null;
        log.debug("Try to find event by id '{}'", id);
        try {
            event = jdbcTemplate.queryForObject(
                    EVENT_FIND_BY_ID,
                    new Object[]{id}, new EventRowMapper()
            );
        } catch (DataAccessException e) {
            log.error("Query fails by finding event with id '{}'", id);
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (event == null) {
            log.debug("Event with id '{}' not found", id);
        } else {
            log.debug("Event with id '{}' was found", id);
            log.debug("Try to set Participants for event with id '{}'", id);
            event.setParticipants(getParticipants(event));
            log.debug("Setting participants for event with id '{}' successful", id);
        }

        return event;
    }

    @Override
    public Event insert(Event model) {
        int id;
        log.debug("Try to insert event with name '{}' by owner with id '{}'", model.getName(), model.getOwnerId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("EVENT")
                .usingGeneratedKeyColumns("EVENT_ID");

        if (model.getImageFilepath() == null) {
            model.setImageFilepath(env.getProperty("image.default.filepath"));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", model.getName());

        if (model.getEventType() != EventType.NOTE) {
            parameters.put("EVENT_DATE", model.getEventDate());
            parameters.put("PERIODICITY_ID", model.getPeriodicityId());
        }
        parameters.put("DESCRIPTION", model.getDescription());
        parameters.put("PLACE", model.getPlace());
        parameters.put("EVENT_TYPE_ID", model.getEventTypeId());
        //System.out.println(model.isDraft());
        parameters.put("IS_DRAFT", model.isDraft() ? 1 : 0);
        parameters.put("FOLDER_ID", model.getFolderId());
        parameters.put("IMAGE_FILEPATH", model.getImageFilepath());
        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setEventId(id);
        } catch (DataAccessException e) {
            log.error("Query fails by insert event with name '{}' by owner with id '{}'", model.getName(), model.getOwnerId());
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (model.getEventId() != 0) {
            log.debug("Event with name '{}' by owner with id '{}' was inserted with id '{}'", model.getName(), model.getOwnerId(), id);
        } else {
            log.debug("Event with name '{}' by owner with id '{}' inserting failed", model.getEventId(), model.getOwnerId());
        }
        return model;
    }

    /**
     * create event with this method, not insert
     *
     * @param model  event
     * @param userId owner id
     * @return created event with id
     */
    @Override
    public Event createEvent(Event model, int userId) {
        log.debug("Try to create event with name '{}' by user with id '{}'", model.getName(), userId);
        Event event;
        // TODO: 30.04.2018 maybe add more logging
        event = insert(model);
        insertUserEvent(userId, model.getEventId(), ownerId);

        return event;
    }

    @Override
    public Role getRole(int userId, int eventId) {
        log.debug("Try to get role for user with id '{}' for event with id '{}'", userId, eventId);
        Role role = null;

        try {
            String roleString = jdbcTemplate.queryForObject(
                    GET_ROLE, new Object[]{userId, eventId},
                    String.class);

            role = Role.valueOf(roleString);
        } catch (DataAccessException e) {
            log.error("Query fails by get role for user with id '{}' for event with id '{}'", userId, eventId);
            System.out.println(e.getMessage());
        }
        if (role != null) {
            log.debug("Role for user with id '{}' for event with id '{}' is ", userId, eventId, role.toString());
        }
        return role;
    }

    private void insertUserEvent(int userId, int eventId, int roleId) {
        log.debug("Try to insert user event with user id '{}', event id '{}', role id '{}'", userId, eventId, roleId);
        int result = 0;
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("USER_EVENT");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("USER_ID", userId);
        parameters.put("EVENT_ID", eventId);
        parameters.put("ROLE_ID", roleId);

        try {
            result = simpleJdbcInsert.execute(parameters);
        } catch (DataAccessException e) {
            log.error("Query fails by insert user event with user id '{}', event id '{}', role id '{}'", userId, eventId, roleId);
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (result == 0) {
            log.debug("Insert user event with user id '{}', event id '{}', role id '{}' not successful", userId, eventId, roleId);
        } else {
            log.debug("Insert user event with user id '{}', event id '{}', role id '{}' successful", userId, eventId, roleId);
        }
    }

    public void addParticipant(int participantId, int eventId) {
        insertUserEvent(participantId, eventId, this.participantId);
    }

    @Override
    public Event update(Event model) {
        log.debug("Try to update event with id '{}'", model.getEventId());
        int result = 0;
        try {
            result = jdbcTemplate.update(EVENT_UPDATE,
                    model.getName(), model.getEventDate(), model.getDescription(), model.getPeriodicityId(),
                    model.getPlace(), model.getEventTypeId(), model.isDraft() ? 1 : 0, model.getFolderId(), model.getImageFilepath(), model.getEventId());
        } catch (DataAccessException e) {
            log.error("Query fails by update event with id '{}'", model.getEventId());
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (result == 0) {
            log.debug("Update event with id '{}' not successful", model.getEventId());
        } else {
            log.debug("Update event with id '{}' successful", model.getEventId());
        }
        return model;
    }

    @Override
    public Event delete(Event model) {
        log.debug("Try to delete event with id '{}'", model.getEventId());
        int result = 0;
        try {
            result = jdbcTemplate.update(EVENT_DELETE, model.getEventId());
        } catch (DataAccessException e) {
            log.error("Query fails by delete event with id '{}'", model.getEventId());
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (result == 0) {
            log.debug("Event with id '{}' was not deleted successful", model.getEventId());
        } else {
            log.debug("Event with id '{}' was deleted successful", model.getEventId());
        }
        return model;
    }

    @Override
    public List<Event> findByFolderId(int folderId) {
        List<Event> events = new ArrayList<>();
        log.debug("Try to find events with folder id '{}'", folderId);
        try {
            events = jdbcTemplate.query(EVENT_FIND__BY_FOLDER_ID,
                    new Object[]{folderId}, new EventRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by find event by folder id '{}'", folderId);
            System.out.println(e.getMessage());
        }
        if (events.isEmpty()) {
            log.debug("Event wasnt found with folder id '{}'", folderId);
            return null;
        } else {
            log.debug("Events was found with folder id '{}' and counted '{}' pcs", folderId, events.size());
            return events;
        }
    }

    @Override
    public List<Event> getDrafts(int folderId) {
        log.debug("Try to get drafts with folder id '{}'", folderId);
        List<Event> events = new ArrayList<>();

        try {
            events = jdbcTemplate.query(EVENT_GET_DRAFTS,
                    new Object[]{folderId}, new EventRowMapper());
        } catch (DataAccessException e) {
            // TODO: 30.04.2018 add exception
            log.error("Query fails by getting drafts with folder id '{}'", folderId);
            System.out.println(e.getMessage());
        }
        if (events.isEmpty()) {
            log.debug("Drafts with folder id '{}' were not founded", folderId);
            return null;
        } else {
            log.debug("Drafts with folder id '{}' were founded counted '{}' pcs", folderId, events.size());
            return events;
        }
    }

    @Override
    public List<Event> findByType(String eventType, int folderId) {
        List<Event> events = new ArrayList<>();
        log.debug("Try to find events with type '{}' with folderId '{}'", eventType, folderId);
        try {
            events = jdbcTemplate.query(EVENT_FIND_BY_TYPE_IN_FOLDER,
                    new Object[]{eventType, folderId}, new EventRowMapper());
            // TODO: 30.04.2018 add exception
        } catch (DataAccessException e) {
            log.error("Query fails by finding events with type '{}' with folderId '{}'", eventType, folderId);
            System.out.println(e.getMessage());
        }
        if (events.isEmpty()) {
            log.debug("Events not found with type '{}' with wolderId '{}'", eventType, folderId);
        } else {
            log.debug("Events were found with type '{}' with wolderId '{}' counted '{}' pcs", eventType, folderId, events.size());
        }
        return events;
    }

    @Override
    public List<User> getParticipants(Event event) {
        log.debug("Try to get participants for event with id '{}'", event.getEventId());
        List<User> participants = new ArrayList<>();

        try {
            participants = jdbcTemplate.query(EVENT_GET_PARTICIPANTS,
                    new Object[]{event.getEventId()}, new UserRowMapper());
            // TODO: 30.04.2018 add exception
        } catch (DataAccessException e) {
            log.error("Query fails by getting participants for event with id '{}'", event.getEventId());
            System.out.println(e.getMessage());
        }

        if (participants.isEmpty()) {
            log.debug("Participants for event with id '{}' not found", event.getEventId());
            participants = null;
        } else {
            log.debug("Participants for event with id '{}' found and counted '{}'", event.getEventId(), participants.size());
        }

        return participants;

    }
}
