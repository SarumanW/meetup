package com.meetup.meetup.service.vm;

import com.meetup.meetup.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
    private boolean confirmed;
    private User user;
}
