package com.meetup.meetup.service.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatIdsVM {
    private int privateChatId;
    private int publicChatId;
}
