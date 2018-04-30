package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.DatabaseWorkException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private static Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final UserDao userDao;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public ProfileService(UserDao userDao, AuthenticationFacade authenticationFacade) {
        this.userDao = userDao;
        this.authenticationFacade = authenticationFacade;
    }

    public User getUser(int id) {
        return userDao.findById(id);
    }

    public List<User> searchUsers(String login, String name, String surname, Integer limit) {

        log.debug("Trying to search users by features login '{}', name '{}', surname '{}' and limit '{}'",
                login, name, surname, limit);

        List<User> users = userDao.findByParams(login, name, surname, limit);

        if (users == null) {
            log.error("Cannot get user from database");
            throw new DatabaseWorkException();
        }

        log.debug("Found users '{}'", users);

        return users;
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

    public boolean addFriend(String userName){
        User user = authenticationFacade.getAuthentication();
        User friend = userDao.findByLogin(userName);
        if(friend != null) {
            return userDao.addFriend(user.getId(), friend.getId());
        }
        return false;
    }

    public int confirmFriend(int friendId){
        User user = authenticationFacade.getAuthentication();
        return userDao.confirmFriend(user.getId(), friendId);
    }

    public int deleteFriend(int id){
        User user = authenticationFacade.getAuthentication();
        return userDao.deleteFriend(user.getId(), id);
    }

    public List<User> getUnknownUsers(String userName){
        User user = authenticationFacade.getAuthentication();
        return userDao.getNotFriends(user.getId(),userName);
    }
}