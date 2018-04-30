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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/events")
public class EventController {

    private static Logger log = LoggerFactory.getLogger(EventController.class);

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
    public ResponseEntity<List<Event>> getEventsByUser(@PathVariable int userId){
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

    @PostMapping("/add")
    public ResponseEntity<Event> addEvent(@Valid @RequestBody Event event) {
        log.debug("Trying to save event '{}'", event.toString());

        Event responseEvent = eventService.addEvent(event);

        log.debug("Send response body event '{}' and status OK", responseEvent.toString());

        return new ResponseEntity<>(responseEvent, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Event> updateEvent(@Valid @RequestBody Event event) {
        log.debug("Trying to update event '{}'", event.toString());

        Event updatedEvent = eventService.updateEvent(event);

        log.debug("Send response body event '{}' and status OK", updatedEvent.toString());

        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Event> deleteEvent(@Valid @RequestBody Event event) {
        log.debug("Trying to delete event '{}'", event.toString());

        Event deletedEvent = eventService.deleteEvent(event);

        log.debug("Send response body event '{}' and status OK", deletedEvent.toString());

        return new ResponseEntity<>(deletedEvent, HttpStatus.OK);
    }

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
        HttpStatus httpStatus;
        try {
            message = eventImageService.store(file);;
            log.debug("Image successfully uploaded send response status OK");
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            log.error(message);
            httpStatus = HttpStatus.EXPECTATION_FAILED;
        }
        return new ResponseEntity<>(message, httpStatus);
    }
}
