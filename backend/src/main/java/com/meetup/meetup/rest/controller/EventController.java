package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile/{id}/folders/{folderId}")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/events/{eventId}")
    public Event getEvent(@PathVariable int id, @PathVariable int folderId, @PathVariable int eventId) {
        Event event = eventService.findById(eventId);
        System.out.println(event);
        return event;
    }
}
