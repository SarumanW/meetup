package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.*;
import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import com.meetup.meetup.exception.runtime.frontend.detailed.LoginNotFoundException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meetup.meetup.keys.Key.EXCEPTION_ENTITY_NOT_FOUND;
import static com.meetup.meetup.keys.Key.EXCEPTION_LOGIN_NOT_FOUND;

@Service
@PropertySource("classpath:strings.properties")
public class EventService {

    private static Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventDao eventDao;
    private final AuthenticationFacade authenticationFacade;
    private final UserDao userDao;
    private final Map<EventPeriodicity, Integer> periodicityMap;
    private final Map<EventType, Integer> eventTypeMap;

    @Autowired
    private Environment env;

    @Autowired
    public EventService(EventDao eventDao, AuthenticationFacade authenticationFacade, UserDao userDao) {
        this.eventDao = eventDao;
        this.authenticationFacade = authenticationFacade;
        this.userDao = userDao;

        this.periodicityMap = new HashMap<>();
        periodicityMap.put(EventPeriodicity.ONCE, 6);
        periodicityMap.put(EventPeriodicity.HOUR, 1);
        periodicityMap.put(EventPeriodicity.DAY, 2);
        periodicityMap.put(EventPeriodicity.WEEK, 3);
        periodicityMap.put(EventPeriodicity.MONTH, 4);
        periodicityMap.put(EventPeriodicity.YEAR, 5);

        this.eventTypeMap = new HashMap<>();
        eventTypeMap.put(EventType.EVENT, 1);
        eventTypeMap.put(EventType.NOTE, 2);
        eventTypeMap.put(EventType.PRIVATE_EVENT, 3);
    }

    public Event getEvent(int eventId) {
        log.debug("Trying to get event from dao by eventId '{}'", eventId);

        Event event = eventDao.findById(eventId);

        if (event == null) {
            log.error("Event was not found by eventId '{}'", eventId);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"Event", "eventId", eventId));
        }

        log.debug("Found event '{}'", event.toString());

        return event;
    }

    public List<Event> getEventsByUser(int userId){
        return eventDao.findByUserId(userId);
    }

    public List<Event> getEventsByType(String eventType, int folderID) {
        return eventDao.findByType(eventType, folderID);
    }

    public List<Event> getFolderEvents(int folderId) {
        log.debug("Trying to get events from dao by folderId '{}'", folderId);

        List<Event> events = eventDao.findByFolderId(folderId);

        if (events == null) {
            log.error("Events was not found by   folderId '{}'", folderId);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"Events", "folderId", folderId));
        }

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getDrafts(int folderId) {
        return eventDao.getDrafts(folderId);
    }

    public Event addEvent(Event event) {
        log.debug("Trying to insert event '{}' to database", event.toString());

        User user = authenticationFacade.getAuthentication();

        int eventTypeId = eventTypeMap.get(event.getEventType());

        int eventPeriodicityId = periodicityMap.get(event.getPeriodicity());
        event.setPeriodicityId(eventPeriodicityId);
        log.debug("Set eventPeriodicity id '{}'", eventPeriodicityId);


        event.setEventTypeId(eventTypeId);
        log.debug("Set eventType id '{}'", eventTypeId);
        return eventDao.createEvent(event, user.getId());
    }

    public Event updateEvent(Event event) {
        log.debug("Trying to update event '{}' in database", event.toString());

        int eventTypeId = eventTypeMap.get(event.getEventType());

        int eventPeriodicityId = periodicityMap.get(event.getPeriodicity());
        event.setPeriodicityId(eventPeriodicityId);
        log.debug("Set eventPeriodicity id '{}'", eventPeriodicityId);


        event.setEventTypeId(eventTypeId);
        log.debug("Set eventType id '{}'", eventTypeId);

        return eventDao.update(event);
    }

    public Event deleteEvent(int eventId) {
        log.debug("Trying to find event by id '{}'", eventId);

        Event event = getEvent(eventId);

        log.debug("Found event '{}' with id '{}'", event, eventId);

        log.debug("Trying to delete members with eventId '{}' from database", eventId);

        event = eventDao.deleteMembers(event);

        log.debug("Trying to delete eventId '{}' from database", eventId);
        return eventDao.delete(event);
    }

    public User addParticipant(int eventId, String login) {

        log.debug("Trying to add participant with login '{}'", login);

        User user = userDao.findByLogin(login);

        if (user == null) {
            log.error("Can not find user with login '{}'", login);
            throw new LoginNotFoundException(env.getProperty(EXCEPTION_LOGIN_NOT_FOUND));
        }

        eventDao.addParticipant(user.getId(), eventId);

        log.debug("Participant with login '{}' was added", login);
        return user;
    }

    public Event deleteParticipants(int eventId) {
        log.debug("Trying to find event by id '{}'", eventId);

        Event event = getEvent(eventId);

        log.debug("Trying to delete eventId '{}' from database", eventId);

        return eventDao.deleteParticipants(event);
    }

    public int deleteParticipant(int eventId, String login) {
        return eventDao.deleteParticipant(eventId, login);
    }

}
