package com.meetup.meetup.dao;

import com.meetup.meetup.entity.User;

public interface UserDao extends Dao<User> {

    User findByLogin(String login);
    User findByEmail(String email);

}
