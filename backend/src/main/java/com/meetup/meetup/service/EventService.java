package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.exception.EntityNotFoundException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventDao eventDao;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public EventService(EventDao eventDao, AuthenticationFacade authenticationFacade) {
        this.eventDao = eventDao;
        this.authenticationFacade = authenticationFacade;
    }

    public Event getEvent(int eventId) {
        Event event = eventDao.findById(eventId);
        if(event == null) {
            throw new EntityNotFoundException("Event", "eventId", eventId);
        }

        return event;
    }

    public List<Event> getFolderEvents(int folderId) {
        return eventDao.findByFolderId(folderId);
    }

    public Event addEvent(Event event) {
        // TODO: 22.04.2018 Check permission
        return eventDao.insert(event);
    }

    public Event updateEvent(Event event) {
        // TODO: 22.04.2018 Check permission
        return eventDao.update(event);
    }

    public Event deleteEvent(Event event) {
        // TODO: 22.04.2018 Check permission
        return eventDao.delete(event);
    }
}
