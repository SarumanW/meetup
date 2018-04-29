package com.meetup.meetup.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
public class Event {

    private int eventId;
    private String name;
    private String eventDate;
    private String description;
    private int periodicityId;
    private EventPeriodicity periodicity;
    private String place;
    private int eventTypeId;
    private EventType eventType;
    @JsonProperty
    private boolean isDraft;
    private int folderId;
    private String imageFilepath;
    private int ownerId;

    private List<User> participants;

}
