package com.meetup.meetup.service.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatIds {
    private int privateChatId;
    private int publicChatId;
}
