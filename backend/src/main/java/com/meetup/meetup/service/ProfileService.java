package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public User getUser(int id) {
        return userDao.findById(id);
    }

    public User updateUser(User newUser) {
        User updatedUser = userDao.findByLogin(newUser.getLogin()).setState(newUser);
        updatedUser = userDao.update(updatedUser);
        return updatedUser;
    }

    public List<User> getFriends() {
        User user = authenticationFacade.getAuthentication();
        return userDao.getFriends(user.getId());
    }

    public List<User> getFriendsRequests() {
        User user = authenticationFacade.getAuthentication();
        return userDao.getFriendsRequests(user.getId());
    }

    public void addFriend(String userName){
        User user = authenticationFacade.getAuthentication();
        User friend = userDao.findByLogin(userName);
        userDao.addFriend(user.getId(), friend.getId());
    }

    public int confirmFriend(int friendId){
        User user = authenticationFacade.getAuthentication();
        return userDao.confirmFriend(user.getId(), friendId);
    }

    public int deleteFriend(int id){
        User user = authenticationFacade.getAuthentication();
        return userDao.deleteFriend(user.getId(), id);
    }
}