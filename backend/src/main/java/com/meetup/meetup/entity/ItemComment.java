package com.meetup.meetup.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class ItemComment {
    private int commentId;
    private String bodyText;
    private Timestamp postTime;
    @NotNull
    private int authorId;
    @NotNull
    private int itemId;
}
