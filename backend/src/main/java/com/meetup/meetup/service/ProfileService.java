package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.vm.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserDao userDao;

    public User get(String login) {
        return userDao.findByLogin(login);
    }

    public Profile minimal(String login) {
        User user = get(login);
        if (user != null) {
            return new Profile(user.getName(), user.getLastname(), user.getLogin());
        } else {
            return null;
        }

    }

    public Profile detailed(String login) {
        User user = get(login);
        return user == null ? null : new Profile(user);
    }
}
