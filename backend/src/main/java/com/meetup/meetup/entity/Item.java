package com.meetup.meetup.entity;

import lombok.Data;

import javax.validation.constraints.Max;
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

    @Size(max = 1023)
    private String description;

    private String imageFilepath;

    private String link;

    private String dueDate;

    private int likes;

    List<String> tags;

    @NotNull
    private int ownerId;

    private int bookerId;

    private ItemPriority priority;

    private boolean isLike;
}
