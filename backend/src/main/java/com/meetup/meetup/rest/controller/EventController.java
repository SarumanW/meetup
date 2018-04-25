package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Event;
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
    public Event getEvent(@PathVariable int id) {
        return eventService.getEvent(id);
    }

    @GetMapping("/folder/{folderId}")
    public List<Event> getFolderEvents(@PathVariable int folderId) {
        return eventService.getFolderEvents(folderId);
    }

    @PostMapping("/add")
    public ResponseEntity<Event> addEvent(@Valid @RequestBody Event event) {
        return new ResponseEntity<>(eventService.addEvent(event), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public Event updateEvent(@Valid @RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    @PostMapping("/delete")
    public Event deleteEvent(@Valid @RequestBody Event event) {
        return eventService.deleteEvent(event);
    }
}
