package com.meetup.meetup.dao;

import com.meetup.meetup.entity.User;

import java.util.List;

public interface UserDao extends Dao<User> {

    User findByLogin(String login);
    User findByEmail(String email);
    List<Integer> friendsIds(String id);
    boolean updatePassword(User user);
}
