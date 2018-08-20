package com.meetup.meetup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int messageId;

    private int senderId;

    private int chatId;

    @NotBlank
    @Size(min = 1, max = 250)
    private String text;

    private String messageDate;

    private String senderLogin;
}
