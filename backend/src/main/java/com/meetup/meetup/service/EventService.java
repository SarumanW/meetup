package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.*;
import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import com.meetup.meetup.exception.runtime.frontend.detailed.LoginNotFoundException;
import com.meetup.meetup.keys.Key;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final MailService mailService;

    @Autowired
    private Environment env;

    @Autowired
    private PdfCreatService pdfCreatService;

    @Autowired
    public EventService(EventDao eventDao, AuthenticationFacade authenticationFacade, UserDao userDao, MailService mailService) {
        this.eventDao = eventDao;
        this.authenticationFacade = authenticationFacade;
        this.userDao = userDao;
        this.mailService = mailService;

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

    public List<Event> getPublicEvents(int userId, String eventName) {
        log.debug("Trying to get public events by userId '{}'", userId);

        List<Event> events = eventDao.getAllPublic(userId, eventName);

        if (events == null) {
            log.error("Events were not found by userId '{}'", userId);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"Events", "userId", userId));
        }

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getEventsByPeriod(String startDate, String endDate) {
        User user = authenticationFacade.getAuthentication();
        int userId = user.getId();

        log.debug("Trying to get events from dao by userId '{}'", userId);
        List<Event> events = eventDao.getPeriodEvents(userId, startDate, endDate);

        if (events == null) {
            log.error("Events was not found by userId '{}'", userId);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"Events", "userId", userId));
        }

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getEventsByPeriodForAllUsers(String startDate, String endDate) {

        log.debug("Trying to get events from dao ");
        List<Event> events = eventDao.getPeriodEventsAllUsers(startDate, endDate);

        if (events == null) {
            log.error("Events was not found");
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"Events", "period", "one day"));
        }

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getDrafts(int folderId) {
        return eventDao.getDrafts(folderId);
    }

    @Transactional
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

    public void sendEventPlan(MultipartFile file) {
        User user = authenticationFacade.getAuthentication();
        Path rootLocation = Paths.get(".");
        try {
            log.debug("Try to copy {} to local storage", file.getOriginalFilename());
            Files.deleteIfExists(rootLocation.resolve(file.getOriginalFilename()));
            Files.copy(file.getInputStream(),rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("Try send mail with file");
        mailService.sendMailWithEventPlan(user,file);
    }

    public Event pinEvent(int eventId) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");
        int userId = user.getId();

        log.debug("Trying to pin event with id '{}' by userId '{}'", eventId, userId);
        return eventDao.pinEvent(userId, eventId);
    }

    public Event unpinEvent(int eventId) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");
        int userId = user.getId();

        log.debug("Trying to unpin event with id'{}' by userId '{}'", eventId, userId);
        return eventDao.unpinEvent(userId, eventId);
    }
}
