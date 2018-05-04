package com.meetup.meetup.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class Item {

    private int itemId;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(min = 4, max = 250)
    private String description;

    @NotNull
    private int ownerId;

    private int bookerId;

    private String imageFilepath;

    private String link;

    private String dueDate;

    private String priority;

    private int likes;

    List<String> tags;
}
