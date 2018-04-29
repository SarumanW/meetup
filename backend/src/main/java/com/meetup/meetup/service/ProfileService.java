package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.EntityNotFoundException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private static Logger log = LoggerFactory.getLogger(ProfileService.class);

    public User getUserByLogin(String login) {
        User user = userDao.findByLogin(login);

        if(user == null) {
            log.error("User was not found by userLogin '{}'", login);
            throw new EntityNotFoundException("User", "userLogin", login);
        }

        log.debug("Found user '{}'", user.toString());

        return user;
    }

    public User updateUser(User newUser) {
        return userDao.update(newUser);
    }

    public List<User> getFriends() {
        User user = authenticationFacade.getAuthentication();

        log.debug("Authenticated user '{}'", user.toString());

        return userDao.getFriends(user.getId());
    }

    public List<User> getFriendsRequests() {
        User user = authenticationFacade.getAuthentication();

        log.debug("Authenticated user '{}'", user.toString());

        return userDao.getFriendsRequests(user.getId());
    }

    public boolean addFriend(int friendId){
        User user = authenticationFacade.getAuthentication();

        log.debug("Authenticated user '{}'", user.toString());

        User friend = userDao.findById(friendId);

        log.debug("Friend found '{}'", friend.toString());

        return friend != null && userDao.addFriend(user.getId(), friend.getId());
    }

    public void confirmFriend(int friendId){
        User user = authenticationFacade.getAuthentication();

        log.debug("Authenticated user '{}'", user.toString());

        if(userDao.confirmFriend(user.getId(), friendId) == user.getId()){
            log.debug("Friend successfully confirmed ");
        }
    }

    public void deleteFriend(int friendId){
        User user = authenticationFacade.getAuthentication();

        log.debug("Authenticated user '{}'", user.toString());

        if(userDao.deleteFriend(user.getId(), friendId)== user.getId()){
            log.debug("Friend successfully deleted ");
        }
    }
}