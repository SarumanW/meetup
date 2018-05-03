package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.rowMappers.EventRowMapper;
import com.meetup.meetup.dao.rowMappers.UserRowMapper;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.EventType;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
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
import com.meetup.meetup.dao.AbstractDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@PropertySource("classpath:sqlDao.properties")
@PropertySource("classpath:strings.properties")
@PropertySource("classpath:image.properties")
public class EventDaoImpl extends AbstractDao<Event> implements EventDao {

    private static Logger log = LoggerFactory.getLogger(EventDaoImpl.class);

    @Autowired
    private UserDao userDao;



    private final int ownerId = 1;
    private final int participantId = 2;


    @Override
    public List<Event> findByUserId(int userId) {
        List<Event> events;
        log.debug("Try to find list of events by user with id '{}'", userId);
        try {
            events = jdbcTemplate.query(env.getProperty(EVENT_FIND_BY_USER_ID),
                    new Object[]{userId}, new EventRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by finding event by user with id '{}'", userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

            log.debug("Events for user with id '{}' counted '{}'", userId, events.size());
        return events;
    }

    @Override
    public Event findById(int id) {
        Event event = null;
        log.debug("Try to find event by id '{}'", id);
        try {
            event = jdbcTemplate.queryForObject(
                    env.getProperty(EVENT_FIND_BY_ID),
                    new Object[]{id}, new EventRowMapper()
            );
        } catch (DataAccessException e) {
            log.error("Query fails by finding event with id '{}'", id);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));

        }

            log.debug("Event with id '{}' was found", id);
            log.debug("Try to set Participants for event with id '{}'", id);
            event.setParticipants(getParticipants(event));
            log.debug("Setting participants for event with id '{}' successful", id);

        return event;
    }

    @Override
    public Event insert(Event model) {
        int id;
        log.debug("Try to insert event with name '{}' by owner with id '{}'", model.getName(), model.getOwnerId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_EVENT)
                .usingGeneratedKeyColumns(EVENT_EVENT_ID);

        if (model.getImageFilepath() == null) {
            model.setImageFilepath(env.getProperty("image.default.filepath"));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EVENT_NAME, model.getName());

        if (model.getEventType() != EventType.NOTE) {
            parameters.put(EVENT_EVENT_DATE, model.getEventDate());
            parameters.put(EVENT_PERIODICITY_ID, model.getPeriodicityId());
        }
        parameters.put(EVENT_DESCRIPTION, model.getDescription());
        parameters.put(EVENT_PLACE, model.getPlace());
        parameters.put(EVENT_EVENT_TYPE_ID, model.getEventTypeId());
        parameters.put(EVENT_IS_DRAFT, model.isDraft() ? 1 : 0);
        parameters.put(EVENT_FOLDER_ID, model.getFolderId());
        parameters.put(EVENT_IMAGE_FILEPATH, model.getImageFilepath());
        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setEventId(id);
        } catch (DataAccessException e) {
            log.error("Query fails by insert event with name '{}' by owner with id '{}'", model.getName(), model.getOwnerId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
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

        event = insert(model);
        insertUserEvent(userId, model.getEventId(), ownerId);

        return event;
    }

    @Override
    public Role getRole(int userId, int eventId) {
        log.debug("Try to get role for user with id '{}' for event with id '{}'", userId, eventId);
        Role role;

        try {
            String roleString = jdbcTemplate.queryForObject(
                    env.getProperty(GET_ROLE), new Object[]{userId, eventId},
                    String.class);

            role = Role.valueOf(roleString);
        } catch (DataAccessException e) {
            log.error("Query fails by get role for user with id '{}' for event with id '{}'", userId, eventId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        log.debug("Role for user with id '{}' for event with id '{}' is ", userId, eventId, role.toString());
        return role;
    }

    private void insertUserEvent(int userId, int eventId, int roleId) {
        log.debug("Try to insert user event with user id '{}', event id '{}', role id '{}'", userId, eventId, roleId);
        int result = 0;
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_USER_EVENT);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(USER_EVENT_USER_ID, userId);
        parameters.put(USER_EVENT_EVENT_ID, eventId);
        parameters.put(USER_EVENT_ROLE_ID, roleId);

        try {
            result = simpleJdbcInsert.execute(parameters);
        } catch (DataAccessException e) {
            log.error("Query fails by insert user event with user id '{}', event id '{}', role id '{}'", userId, eventId, roleId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
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
            result = jdbcTemplate.update(env.getProperty(EVENT_UPDATE),
                    model.getName(), model.getEventDate(), model.getDescription(), model.getPeriodicityId(),
                    model.getPlace(), model.getEventTypeId(), model.isDraft() ? 1 : 0, model.getFolderId(), model.getImageFilepath(), model.getEventId());
        } catch (DataAccessException e) {
            log.error("Query fails by update event with id '{}'", model.getEventId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
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
            result = jdbcTemplate.update(env.getProperty(EVENT_DELETE), model.getEventId());
        } catch (DataAccessException e) {
            log.error("Query fails by delete event with id '{}'", model.getEventId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
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
            events = jdbcTemplate.query(env.getProperty(EVENT_FIND_BY_FOLDER_ID),
                    new Object[]{folderId}, new EventRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by find event by folder id '{}'", folderId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
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
            events = jdbcTemplate.query(env.getProperty(EVENT_GET_DRAFTS),
                    new Object[]{folderId}, new EventRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by getting drafts with folder id '{}'", folderId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
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
            events = jdbcTemplate.query(env.getProperty(EVENT_FIND_BY_TYPE_IN_FOLDER),
                    new Object[]{eventType, folderId}, new EventRowMapper());

        } catch (DataAccessException e) {
            log.error("Query fails by finding events with type '{}' with folderId '{}'", eventType, folderId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
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
            participants = jdbcTemplate.query(env.getProperty(EVENT_GET_PARTICIPANTS),
                    new Object[]{event.getEventId()}, new UserRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by getting participants for event with id '{}'", event.getEventId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
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
