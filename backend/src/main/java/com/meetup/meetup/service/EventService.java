package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.EntityNotFoundException;
import com.meetup.meetup.exception.LoginNotFoundException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
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
        Event event = eventDao.findById(eventId);
        if(event == null) {
            throw new EntityNotFoundException("Event", "eventId", eventId);
        }

        return event;
    }

    public List<Event> getEventsByType(String eventType, int folderID) {
        return eventDao.findByType(eventType, folderID);
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
