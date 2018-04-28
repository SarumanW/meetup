package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable int id) {
        return new ResponseEntity<>(eventService.getEvent(id), HttpStatus.OK);
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<Event>> getFolderEvents(@PathVariable int folderId) {
        return new ResponseEntity<>(eventService.getFolderEvents(folderId), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Event> addEvent(@Valid @RequestBody Event event) {
        return new ResponseEntity<>(eventService.addEvent(event), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Event> updateEvent(@Valid @RequestBody Event event) {
        return new ResponseEntity<>(eventService.updateEvent(event), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Event> deleteEvent(@Valid @RequestBody Event event) {
        return new ResponseEntity<>(eventService.deleteEvent(event), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/participant/add")
    public ResponseEntity<User> addParticipant(@PathVariable int eventId, @RequestBody String login) {
        return new ResponseEntity<>(eventService.addParticipant(eventId, login), HttpStatus.CREATED);
    }
}
