package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.exception.EntityNotFoundException;
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

    @Autowired
    public EventService(EventDao eventDao, AuthenticationFacade authenticationFacade) {
        this.eventDao = eventDao;
        this.authenticationFacade = authenticationFacade;
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

    public Event addEvent(Event event) {
        log.debug("Trying to insert event '{}' to database", event.toString());

        // TODO: 22.04.2018 Check permission
        return eventDao.insert(event);
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
}
