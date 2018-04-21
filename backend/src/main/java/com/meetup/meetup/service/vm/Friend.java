package com.meetup.meetup.service.vm;

import com.meetup.meetup.entity.User;
import lombok.Data;

@Data
public class Friend extends User {
    private boolean confirmed;
}
