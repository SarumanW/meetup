package com.meetup.meetup.service.vm;

import com.meetup.meetup.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatCheckEntity {
    private Role role;
    private int chatTypeId;
}
