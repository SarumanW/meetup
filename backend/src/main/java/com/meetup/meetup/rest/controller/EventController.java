package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.frontend.detailed.FileUploadException;
import com.meetup.meetup.service.EventImageService;
import com.meetup.meetup.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/events")
@PropertySource("classpath:strings.properties")
public class EventController {

    private static Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private Environment env;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventImageService eventImageService;

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable int id) {
        log.debug("Trying to get event by id '{}'", id);

        Event event = eventService.getEvent(id);

        log.debug("Send response body event '{}' and status OK", event.toString());

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Event>> getEventsByUser(@PathVariable int userId) {
        log.debug("Trying to get event by userId '{}'", userId);

        List<Event> userEvents = eventService.getEventsByUser(userId);

        log.debug("Send response body events '{}' and status OK", userEvents.toString());

        return new ResponseEntity<>(userEvents, HttpStatus.OK);
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<Event>> getFolderEvents(@PathVariable int folderId) {
        log.debug("Trying to get event by folderId '{}'", folderId);

        List<Event> events = eventService.getFolderEvents(folderId);

        log.debug("Send response body events '{}' and status OK", events.toString());

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PreAuthorize("@eventPermissionChecker.canCreateEvent(#event)")
    @PostMapping("/add")
    public ResponseEntity<Event> addEvent(@Valid @RequestBody Event event) {
        log.debug("Trying to save event '{}'", event.toString());

        Event responseEvent = eventService.addEvent(event);

        log.debug("Send response body event '{}' and status OK", responseEvent.toString());

        return new ResponseEntity<>(responseEvent, HttpStatus.CREATED);
    }

    @PreAuthorize("@eventPermissionChecker.canUpdateEvent(#event)")
    @PutMapping
    public ResponseEntity<Event> updateEvent(@Valid @RequestBody Event event) {
        log.debug("Trying to update event '{}'", event.toString());

        Event updatedEvent = eventService.updateEvent(event);

        log.debug("Send response body event '{}' and status OK", updatedEvent.toString());

        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @PreAuthorize("@eventPermissionChecker.canDeleteEvent(#eventId)")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Event> deleteEvent(@PathVariable int eventId) {
        log.debug("Trying to delete eventId '{}'", eventId);

        Event deletedEvent = eventService.deleteEvent(eventId);

        log.debug("Send response body event '{}' and status OK", deletedEvent.toString());

        return new ResponseEntity<>(deletedEvent, HttpStatus.OK);
    }

    @PreAuthorize("@eventPermissionChecker.canDeleteEvent(#eventId)")
    @PostMapping("/{eventId}/participant/add")
    public ResponseEntity<User> addParticipant(@PathVariable int eventId, @RequestBody String login) {
        return new ResponseEntity<>(eventService.addParticipant(eventId, login), HttpStatus.CREATED);
    }

    @GetMapping("/{folderId}/getByType/{eventType}")
    public ResponseEntity<List<Event>> getByType(@PathVariable String eventType, @PathVariable int folderId) {
        return new ResponseEntity<>(eventService.getEventsByType(eventType, folderId), HttpStatus.OK);
    }

    @GetMapping("/{folderId}/drafts")
    public ResponseEntity<List<Event>> getDrafts(@PathVariable int folderId) {
        return new ResponseEntity<>(eventService.getDrafts(folderId), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.debug("Trying to upload event image '{}'", file);

        String message;
        message = eventImageService.store(file);
        log.debug("Image successfully uploaded send response status OK");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PreAuthorize("@eventPermissionChecker.canDeleteEvent(#eventId)")
    @DeleteMapping("/participants/{eventId}")
    public ResponseEntity<Event> deleteParticipants(@PathVariable int eventId) {
        log.debug("Trying to delete participants of eventId '{}'", eventId);

        Event deletedParticipantsEvent = eventService.deleteParticipants(eventId);

        log.debug("Send response body event '{}' and status OK", deletedParticipantsEvent);

        return new ResponseEntity<>(deletedParticipantsEvent, HttpStatus.OK);
    }

    @PreAuthorize("@eventPermissionChecker.canDeleteEvent(#eventId)")
    @DeleteMapping("{eventId}/participant/{login}")
    public ResponseEntity<String> deleteParticipant(@PathVariable int eventId, @PathVariable String login) {
        HttpStatus httpStatus;
        String message;
        log.debug("Trying to delete participant with login {} of eventId '{}'", login, eventId);

        int result = eventService.deleteParticipant(eventId, login);

        if (result == 0) {
            httpStatus = HttpStatus.NOT_FOUND;
            message = "\"Participant with this login does not exist\"";
        } else {
            httpStatus = HttpStatus.OK;
            message = "\"Participant was deleted successfully\"";
        }

        log.debug("Send response body event '{}' and status {}", message, httpStatus);

        return new ResponseEntity<String>(message, httpStatus);
    }
}
