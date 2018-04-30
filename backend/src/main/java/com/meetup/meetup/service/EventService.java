package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.*;
import com.meetup.meetup.exception.EntityNotFoundException;
import com.meetup.meetup.exception.LoginNotFoundException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private static Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventDao eventDao;
    private final AuthenticationFacade authenticationFacade;
    private final UserDao userDao;
    private final Map<EventPeriodicity, Integer> periodicityMap;
    private final Map<EventType, Integer> eventTypeMap;

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
            throw new EntityNotFoundException("Event", "eventId", eventId);
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
            log.error("Events was not found by folderId '{}'", folderId);
            throw new EntityNotFoundException("Events", "folderId", folderId);
        }

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getDrafts(int folderId) {
        return eventDao.getDrafts(folderId);
    }

    public Event addEvent(Event event) {
        log.debug("Trying to insert event '{}' to database", event.toString());

        // TODO: 22.04.2018 Check permission
        User user = authenticationFacade.getAuthentication();
        int eventTypeId = eventTypeMap.get(event.getEventType());

        if (event.getEventType() != EventType.NOTE) {
            int eventPeriodicityId = periodicityMap.get(event.getPeriodicity());
            event.setPeriodicityId(eventPeriodicityId);
            log.debug("Set eventPeriodicity id '{}'", eventPeriodicityId);
        }

        event.setEventTypeId(eventTypeId);
        log.debug("Set eventType id '{}'", eventTypeId);
        return eventDao.createEvent(event, user.getId());
    }

    public Event updateEvent(Event event) {
        log.debug("Trying to update event '{}' in database", event.toString());

        // TODO: 22.04.2018 Check permission
        return eventDao.update(event);
    }

    public Event deleteEvent(Event event) {
        log.debug("Trying to delete event '{}' from database", event.toString());

        // TODO: 22.04.2018 Check permission
        return eventDao.delete(event);
    }

    public User addParticipant(int eventId, String login) {

        log.debug("Trying to add paritipant with login '{}'", login);
        checkPermission(eventId);

        User user = userDao.findByLogin(login);

        if (user == null) {
            log.debug("Can not find user with login '{}'", login);
            throw new LoginNotFoundException();
        }

        eventDao.addParticipant(user.getId(), eventId);

        log.debug("Participant with login '{}' was added", login);
        return user;
    }

    //Check authentication and folder permission
    private void checkPermission(int eventId) {
        User user = authenticationFacade.getAuthentication();

        if (eventDao.getRole(user.getId(), eventId) != Role.OWNER) {
            log.debug("User with id '{}' has not permission to add participant to event '{}'", user.getId(), eventId);
            throw new EntityNotFoundException("Event", "eventId", eventId);
        }
    }
}
