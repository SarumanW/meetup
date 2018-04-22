package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserDao userDao;

    public User get(String login) {
        return userDao.findByLogin(login);
    }

    public User getById(int id) {
        return userDao.findById(id);
    }
}
