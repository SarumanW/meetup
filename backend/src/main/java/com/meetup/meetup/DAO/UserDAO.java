package com.meetup.meetup.DAO;

import com.meetup.meetup.rest.model.User;

public interface UserDAO {
    interface CustomerDAO {
        int insertUser(User user);
        boolean deleteUser(User user);
        User findUserByUserName(String byUserName);
        User findUserByMail(String byMail);
        User findUserById(String byUserId);
    }
}
