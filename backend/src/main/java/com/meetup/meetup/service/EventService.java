package com.meetup.meetup.service;

import com.meetup.meetup.dao.ChatDao;
import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.*;
import com.meetup.meetup.exception.runtime.frontend.detailed.LoginNotFoundException;
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
import java.util.List;

import static com.meetup.meetup.keys.Key.EXCEPTION_LOGIN_NOT_FOUND;

@Service
@PropertySource("classpath:strings.properties")
public class EventService {

    private static Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    protected Environment env;

    private final PdfCreateService pdfCreateService;
    private final ChatDao chatDao;
    private final EventDao eventDao;
    private final UserDao userDao;
    private final AuthenticationFacade authenticationFacade;
    private final MailService mailService;

    @Autowired
    public EventService(EventDao eventDao, UserDao userDao, MailService mailService, AuthenticationFacade authenticationFacade, PdfCreateService pdfCreateService, ChatDao chatDao) {
        this.chatDao = chatDao;
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.mailService = mailService;
        this.authenticationFacade = authenticationFacade;
        this.pdfCreateService = pdfCreateService;
    }

    public Event getEvent(int eventId) {
        log.debug("Trying to get event from dao by eventId '{}'", eventId);

        Event event = eventDao.findById(eventId);

        log.debug("Found event '{}'", event.toString());

        return event;
    }

    public List<Event> getEventsByUser(int userId){
        log.debug("Trying to get events from dao by userId '{}'", userId);

        List<Event> events = eventDao.findByUserId(userId);

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getEventsByType(int userId, String eventType, int folderId) {
        log.debug("Trying to get events from dao by folderId '{}' and userId '{}'", folderId, userId);

        List<Event> events = eventDao.findByType(userId, eventType, folderId);

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getFolderEvents(int userId, int folderId) {
        log.debug("Trying to get events from dao by folderId '{}' and userId '{}'", folderId, userId);

        List<Event> events = eventDao.findByFolderId(userId, folderId);

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getPublicEvents(int userId, String eventName) {
        log.debug("Trying to get public events by userId '{}'", userId);

        List<Event> events = eventDao.getAllPublic(userId, eventName);

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getEventsByPeriod(int userId, String startDate, String endDate) {
        log.debug("Trying to get events from dao by userId '{}'", userId);

        List<Event> events = eventDao.getPeriodEvents(userId, startDate, endDate);

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getEventsByPeriodForAllUsers(String startDate, String endDate) {
        log.debug("Trying to get events from dao ");

        List<Event> events = eventDao.getPeriodEventsAllUsers(startDate, endDate);

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    public List<Event> getDrafts(int userId, int folderId) {
        log.debug("Trying to get drafts from dao by user id '{}' and folder id '{}'", userId, folderId);

        List<Event> events = eventDao.getDrafts(userId, folderId);

        log.debug("Found events '{}'", events.toString());

        return events;
    }

    @Transactional
    public Event addEvent(int userId, Event event) {
        log.debug("Trying to insert event '{}' to database", event.toString());

        int eventPeriodicityId = event.getPeriodicity().getValue();

        event.setPeriodicityId(eventPeriodicityId);

        log.debug("Set eventPeriodicity id '{}'", eventPeriodicityId);

        int eventTypeId = event.getEventType().getValue();

        event.setEventTypeId(eventTypeId);

        log.debug("Set eventType id '{}'", eventTypeId);

        return eventDao.createEvent(event, userId);
    }

    public Event updateEvent(Event event) {
        log.debug("Trying to update event '{}' in database", event.toString());

        int eventPeriodicityId = event.getPeriodicity().getValue();

        event.setPeriodicityId(eventPeriodicityId);

        log.debug("Set eventPeriodicity id '{}'", eventPeriodicityId);

        int eventTypeId = event.getEventType().getValue();

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

        log.debug("Trying to delete chats with eventId '{}' from database", eventId);

        chatDao.deleteChatsByEventId(eventId);

        log.debug("Try to unpin event with id : '{}', for all users", eventDao.unpinAllOnDelete(eventId));

        log.debug("Try to unpin event with id : '{}', for all users", eventId);

        log.debug("Trying to delete eventId '{}' from database", eventId);
        return eventDao.delete(event);
    }

    public User addParticipant(int ownerId, int eventId, String login) {
        log.debug("Trying to add participant with login '{}'", login);

        User user = userDao.findByLogin(login);

        if (user == null) {
            log.error("Can not find user with login '{}'", login);
            throw new LoginNotFoundException(env.getProperty(EXCEPTION_LOGIN_NOT_FOUND));
        }

        eventDao.addParticipant(ownerId, user.getId(), eventId);

        log.debug("Participant with login '{}' was added", login);

        return user;
    }

    public Event deleteParticipants(int ownerId, int eventId) {
        log.debug("Trying to find event by id '{}'", eventId);

        Event event = getEvent(eventId);

        log.debug("Trying to delete eventId '{}' from database", eventId);

        return eventDao.deleteParticipants(ownerId, event);
    }

    public void deleteParticipant(int ownerId, int eventId, int participantId) {
        log.debug("Trying to delete events from DB by ownerId '{}', eventId '{}' and participantId '{}'", ownerId, eventId, participantId);

        eventDao.deleteParticipant(ownerId, eventId, participantId);
    }

    public void sendEventPlan(MultipartFile file) {
        log.debug("Try to get authenticated user");

        User user = authenticationFacade.getAuthentication();

        log.debug("Get user '{}' successful", user);

        Path rootLocation = Paths.get(".");

        try {
            log.debug("Try to copy {} to local storage", file.getOriginalFilename());
            Files.deleteIfExists(rootLocation.resolve(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            log.error("IOExeption while sending event plan",e);
        }

        log.debug("Try send mail with file");

        mailService.sendMailWithEventPlan(user, file);
    }

    public Event pinEvent(int userId, int eventId) {
        log.debug("Trying to pin event with id '{}' by userId '{}'", eventId, userId);

        return eventDao.pinEvent(userId, eventId);
    }

    public Event unpinEvent(int userId, int eventId) {
        log.debug("Trying to unpin event with id'{}' by userId '{}'", eventId, userId);

        return eventDao.unpinEvent(userId, eventId);
    }
}
