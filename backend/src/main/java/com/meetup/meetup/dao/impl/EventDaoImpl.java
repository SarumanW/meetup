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
        List<Event> events = null;

        try {
            events = jdbcTemplate.query(EVENT_FIND_BY_USER_ID,
                    new Object[]{userId}, new EventRowMapper());
        } catch (DataAccessException e){
            System.out.println(e.getMessage());
        }

        return events;
    }

    @Override
    public Event findById(int id) {
        Event event = null;

        try {
            event = jdbcTemplate.queryForObject(
                    EVENT_FIND_BY_ID,
                    new Object[]{id}, new EventRowMapper()
            );
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        if (event != null) {
            event.setParticipants(getParticipants(event));
        }

        return event;
    }

    @Override
    public Event insert(Event model) {
        int id;

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
        System.out.println(model.isDraft());
        parameters.put("IS_DRAFT", model.isDraft() ? 1 : 0);
        parameters.put("FOLDER_ID", model.getFolderId());
        parameters.put("IMAGE_FILEPATH", model.getImageFilepath());
        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setEventId(id);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        return model;
    }

    /**
     * create event with this method, not insert
     * @param model event
     * @param userId owner id
     * @return created event with id
     */
    @Override
    public Event createEvent(Event model, int userId) {

        Event event;

        event = insert(model);
        insertUserEvent(userId, model.getEventId(), ownerId);

        return event;
    }

    @Override
    public Role getRole(int userId, int eventId) {

        Role role = null;

        try {
            String roleString = jdbcTemplate.queryForObject(
                    GET_ROLE, new Object[] { userId, eventId },
                    String.class);

            role = Role.valueOf(roleString);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return role;
    }

    private void insertUserEvent(int userId, int eventId, int roleId) {


        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("USER_EVENT");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("USER_ID", userId);
        parameters.put("EVENT_ID", eventId);
        parameters.put("ROLE_ID", roleId);

        try {
            simpleJdbcInsert.execute(parameters);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
    }

    public void addParticipant(int participantId, int eventId) {
        insertUserEvent(participantId, eventId, this.participantId);
    }

    @Override
    public Event update(Event model) {
        try {
            jdbcTemplate.update(EVENT_UPDATE,
                    model.getName(), model.getEventDate(), model.getDescription(), model.getPeriodicityId(),
                    model.getPlace(), model.getEventTypeId(), model.isDraft() ? 1 : 0, model.getFolderId(), model.getImageFilepath(), model.getEventId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        return model;
    }

    @Override
    public Event delete(Event model) {
        try {
            jdbcTemplate.update(EVENT_DELETE, model.getEventId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        return model;
    }

    @Override
    public List<Event> findByFolderId(int folderId) {
        List<Event> events = null;

        try {
            events = jdbcTemplate.query(EVENT_FIND__BY_FOLDER_ID,
                    new Object[]{folderId}, new EventRowMapper());
        } catch (DataAccessException e){
            System.out.println(e.getMessage());
        }

        return events;
    }

    @Override
    public List<Event> getDrafts(int folderId) {
        List<Event> events = null;

        try {
            events = jdbcTemplate.query(EVENT_GET_DRAFTS,
                    new Object[]{folderId}, new EventRowMapper());
        } catch (DataAccessException e){
            System.out.println(e.getMessage());
        }

        return events;
    }

    @Override
    public List<Event> findByType(String eventType, int folderId) {
        List<Event> events = null;

        try {
            events = jdbcTemplate.query(EVENT_FIND_BY_TYPE_IN_FOLDER,
                    new Object[]{eventType, folderId}, new EventRowMapper());
        } catch (DataAccessException e){
            System.out.println(e.getMessage());
        }

        System.out.println(events);

        return events;
    }

    @Override
    public List<User> getParticipants(Event event) {

        List<User> participants = new ArrayList<>();

        try {
            participants = jdbcTemplate.query(EVENT_GET_PARTICIPANTS,
                    new Object[]{event.getEventId()}, new UserRowMapper());
        } catch (DataAccessException e) {
            // TODO: 26/04/18  Add logs
            System.out.println(e.getMessage());
        }

        if (participants.isEmpty()) {
            //TODO add logs
            participants = null;
        }

        return participants;

    }
}
