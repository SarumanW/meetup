package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.EntityNotFoundException;
import com.meetup.meetup.exception.LoginNotFoundException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private static Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventDao eventDao;
    private final AuthenticationFacade authenticationFacade;
    private final UserDao userDao;

    @Autowired
    public EventService(EventDao eventDao, AuthenticationFacade authenticationFacade, UserDao userDao) {
        this.eventDao = eventDao;
        this.authenticationFacade = authenticationFacade;
        this.userDao = userDao;
    }

    public Event getEvent(int eventId) {
        log.debug("Trying to get event from dao by eventId '{}'", eventId);

        Event event = eventDao.findById(eventId);

        if(event == null) {
            log.error("Event was not found by eventId '{}'", eventId);
            throw new EntityNotFoundException("Event", "eventId", eventId);
        }

        log.debug("Found event '{}'", event.toString());

        return event;
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

        checkPermission(eventId);

        User user = userDao.findByLogin(login);

        if (user == null) {
            throw new LoginNotFoundException();
        }

        eventDao.addParticipant(user.getId(), eventId);

        return user;
    }

    //Check authentication and folder permission
    private void checkPermission(int eventId) {
        User user = authenticationFacade.getAuthentication();

        if (eventDao.getRole(user.getId(), eventId) != Role.OWNER) {
            throw new EntityNotFoundException("Event", "eventId", eventId);
        }
    }
}
