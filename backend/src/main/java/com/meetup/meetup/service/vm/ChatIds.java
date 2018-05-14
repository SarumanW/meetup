package com.meetup.meetup.service.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatIds {
    private int privateChatId;
    private int publicChatId;

    public ChatIds(List<Integer> ids) {
        if (ids.size() == 2) {
            publicChatId = ids.get(0);
            privateChatId = ids.get(1);
        }
    }
}
