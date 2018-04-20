package com.meetup.meetup.entity;


import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
public class Event {

    private int eventId;
    private String name;
    private Calendar eventDate;
    private String description;
    private EventPeriodicity periodicity;
    private String place;
    private boolean isEvent;
    private boolean isDraft;
    private int folderId;

    private List<User> participants;

}
