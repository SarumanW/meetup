package com.meetup.meetup.entity;


import lombok.Data;

import java.util.List;

@Data
public class Folder {

    private int folderId;
    private String name;
    private int userId;

    private List<Event> events;
}
