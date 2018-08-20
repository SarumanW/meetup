package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.EventImageService;
import com.meetup.meetup.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/users/{userId}/events")
public class EventController {

    private static Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private EventImageService eventImageService;

    @GetMapping
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Event>> getEventsByUser(@PathVariable int userId) {
        log.debug("Trying to get event by userId '{}'", userId);

        List<Event> userEvents = eventService.getEventsByUser(userId);

        log.debug("Send response body events '{}' and status OK", userEvents.toString());

        return new ResponseEntity<>(userEvents, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable int userId, @PathVariable int eventId) {
        log.debug("Trying to get event by id '{}'", eventId);

        Event event = eventService.getEvent(eventId);

        log.debug("Send response body event '{}' and status OK", event);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("@eventAuthorization.isEventCorrect(#userId, #event)")
    public ResponseEntity<Event> addEvent(@PathVariable int userId, @Valid @RequestBody Event event) {
        log.debug("Trying to save event '{}'", event.toString());

        Event responseEvent = eventService.addEvent(userId, event);

        log.debug("Send response body event '{}' and status OK", responseEvent.toString());

        return new ResponseEntity<>(responseEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("@eventAuthorization.isEventCorrect(#userId, #eventId, #event)")
    public ResponseEntity<Event> updateEvent(@PathVariable int userId, @PathVariable int eventId, @Valid @RequestBody Event event) {
        log.debug("Trying to update event '{}'", event.toString());

        Event updatedEvent = eventService.updateEvent(event);

        log.debug("Send response body event '{}' and status OK", updatedEvent.toString());

        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Event> deleteEvent(@PathVariable int userId, @PathVariable int eventId) {
        log.debug("Trying to delete eventId '{}'", eventId);

        Event deletedEvent = eventService.deleteEvent(eventId);

        log.debug("Send response body event '{}' and status OK", deletedEvent.toString());

        return new ResponseEntity<>(deletedEvent, HttpStatus.OK);
    }

    //Participants

    @PostMapping("/{eventId}/participants")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<User> addParticipant(@PathVariable int userId, @PathVariable int eventId, @RequestBody String login) {
        log.debug("Trying to add participant with login '{}' for eventId '{}'", login, eventId);

        User participant = eventService.addParticipant(userId, eventId, login);

        log.debug("Send response body participant '{}' and status OK", participant);

        return new ResponseEntity<>(participant, HttpStatus.CREATED);
    }

    @DeleteMapping("/{eventId}/participants")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Event> deleteParticipants(@PathVariable int userId, @PathVariable int eventId) {
        log.debug("Trying to delete participants of eventId '{}'", eventId);

        Event deletedParticipantsEvent = eventService.deleteParticipants(userId, eventId);

        log.debug("Send response body event '{}' and status OK", deletedParticipantsEvent);

        return new ResponseEntity<>(deletedParticipantsEvent, HttpStatus.OK);
    }

    @DeleteMapping("{eventId}/participants/{participantId}")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<String> deleteParticipant(@PathVariable int userId, @PathVariable int eventId, @PathVariable int participantId) {
        log.debug("Trying to delete participant with id {} of eventId '{}'", participantId, eventId);

        eventService.deleteParticipant(userId, eventId, participantId);
        String message = "\"Participant was deleted successfully\"";
        HttpStatus httpStatus = HttpStatus.OK;

        log.debug("Send response body event '{}' and status {}", message, httpStatus);

        return new ResponseEntity<>(message, httpStatus);
    }

    //Folders events

    @GetMapping("/folders/{folderId}")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Event>> getFolderEvents(@PathVariable int userId, @PathVariable int folderId) {
        log.debug("Trying to get event by folderId '{}'", folderId);

        List<Event> events = eventService.getFolderEvents(userId, folderId);

        log.debug("Send response body events '{}' and status OK", events.toString());

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/folders/{folderId}/type/{type}")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Event>> getByType(@PathVariable int userId, @PathVariable String type, @PathVariable int folderId) {
        log.debug("Trying to get event by folderId '{}' and type '{}'", folderId, type);

        List<Event> events = eventService.getEventsByType(userId, type, folderId);

        log.debug("Send response body events '{}' and status OK", events.toString());

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/folders/{folderId}/drafts")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Event>> getDrafts(@PathVariable int userId, @PathVariable int folderId) {
        log.debug("Trying to get drafts by folderId '{}'", folderId);

        List<Event> drafts = eventService.getDrafts(userId, folderId);

        log.debug("Send response body drafts '{}' and status OK", drafts.toString());

        return new ResponseEntity<>(drafts, HttpStatus.OK);
    }

    //Period

    @GetMapping("/period")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Event>> getInPeriod(@PathVariable int userId,
                                                   @RequestParam("startDate") String startDate,
                                                   @RequestParam("endDate") String endDate) {
        log.debug("Trying to get events by period from '{}' to '{}'", startDate, endDate);

        List<Event> events = eventService.getEventsByPeriod(userId, startDate, endDate);

        log.debug("Send response body events '{}' and status OK", events.toString());

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    //Public

    @GetMapping("/public")
    public ResponseEntity<List<Event>> getPublicEvents(@PathVariable int userId, @RequestParam("name") String name) {
        log.debug("Trying to get user public events by userId '{}'", userId);

        List<Event> events = eventService.getPublicEvents(userId, name);

        log.debug("Send response body events '{}' and status OK", events.toString());

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    //Pinned

    @GetMapping("/{eventId}/pinned")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Event> pinEvent(@PathVariable int userId, @PathVariable int eventId) {
        log.debug("Try to pin event by id '{}', user id '{}' ", eventId, userId);

        Event event = eventService.pinEvent(userId, eventId);

        log.debug("Send response body events '{}' and status OK", event);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}/pinned")
    @PreAuthorize("@eventAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Event> unpinEvent(@PathVariable int userId, @PathVariable int eventId) {
        log.debug("Try to unpin event by id '{}', user id '{}' ", eventId, userId);

        Event event = eventService.unpinEvent(userId, eventId);

        log.debug("Send response body events '{}' and status OK", event);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    //Plan

    @PostMapping("/plan/send")
    public ResponseEntity sendEventPlan(@RequestParam MultipartFile file) {
        log.debug("Try to send {} to email", file.getOriginalFilename());

        eventService.sendEventPlan(file);

        log.debug("Send response status OK");

        return new ResponseEntity(HttpStatus.OK);
    }

    //Uploading files

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.debug("Trying to upload event image '{}'", file);

        String message = eventImageService.store(file);

        log.debug("Image successfully uploaded send response status OK");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
