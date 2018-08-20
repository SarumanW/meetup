package com.meetup.meetup.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public class ItemComment {
    private int commentId;
    @NotBlank
    @Size(max = 2000)
    private String bodyText;
    private Timestamp postTime;
    private String login;
    private String imageFilepath;
    private int authorId;
    private int itemId;
}
