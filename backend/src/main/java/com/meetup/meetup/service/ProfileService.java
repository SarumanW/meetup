package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meetup.meetup.keys.Key.EXCEPTION_ENTITY_NOT_FOUND;

@Service
@PropertySource("classpath:strings.properties")
public class ProfileService {

    private static Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final UserDao userDao;
    private final EventService eventService;

    @Autowired
    public ProfileService(UserDao userDao, EventService eventService) {
        this.userDao = userDao;
        this.eventService = eventService;
    }

    @Autowired
    private Environment env;


    public User getUserByLogin(String login) {
        log.debug("Trying to get user by login '{}' from DB", login);

        User user = userDao.findByLogin(login);

        if (user == null) {
            log.error("User was not found by userLogin '{}'", login);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "User", "userLogin", login));
        }

        log.debug("Found user '{}'", user.toString());

        return user;
    }

    public String getUserLoginById(int userid) {
        log.debug("Trying to get login by id '{}' from DB", userid);

        String login = userDao.findLoginById(userid);

        log.debug("Found user '{}'", login);

        return login;
    }

    public User updateUser(User user) {
        log.debug("Trying to update user '{}' in DB", user);

        User updatedUser = userDao.update(user);

        log.debug("Updated user '{}'", updatedUser);

        return updatedUser;
    }

    public List<User> getFriends(String login) {
        User user = getUserByLogin(login);

        log.debug("User for finding friends '{}'", user);

        return userDao.getFriends(user.getId());
    }

    public List<User> getFriendsRequests(int userId) {
        log.debug("Trying to find requests to friends user '{}' in DB", userId);

        List<User> users = userDao.getFriendsRequests(userId);

        log.debug("Found requests to friends for user '{}'", userId);

        return users;
    }

    public boolean addFriend(int userId, String friendLogin) {
        User friend = getUserByLogin(friendLogin);

        log.debug("Friend found '{}'", friend);

        return userDao.addFriend(userId, friend.getId());
    }

    public void confirmFriend(int userId, int friendId) {

        log.debug("Trying to confirm friend by user '{}' and friend '{}' in DB", userId, friendId);

        if (userDao.confirmFriend(userId, friendId) != 0) {
            log.debug("Friend successfully confirmed");
        }
    }

    public void deleteFriend(int userId, int friendId) {
        log.debug("Trying to delete friend by user '{}' and friend '{}' in DB", userId, friendId);

        if (userDao.deleteFriend(userId, friendId) != 0) {
            log.debug("Friend successfully deleted ");
        }
    }

    public List<User> getUsersByRelationshipType(int userId, String userName, String type) {
        log.debug("Trying to get users by authenticated user '{}', username '{}' and relationship type '{}' in DB", userId, userName, type);

        switch (type) {
            case "unknown":
                return userDao.getPotentialFriendsByUsernamePart(userId, userName);
            case "friends":
                return userDao.getFriendsByUsernamePart(userId, userName);
            default:
                return userDao.getAllByUsernamePart(userName);
        }
    }

    public String userRelations(int userId, int otherUserId) {
        log.debug("Trying to get type of relation by authenticated user '{}', other user '{}' in DB", userId, otherUserId);

        if (userDao.getFriends(userId).contains(userDao.findById(otherUserId))) {
            return "Friends";
        } else if (userDao.getFriendsRequests(userId).contains(userDao.findById(otherUserId))) {
            return "Not Confirmed";
        } else if (userDao.getFriendsRequests(otherUserId).contains(userDao.findById(userId))) {
            return "Request sent";
        } else {
            return "Not Friends";
        }
    }

    public User getProfileWithEvent(String login) {
        log.debug("Trying to get user by login '{}'", login);

        User user = getUserByLogin(login);

        Event event = null;

        log.debug("Trying to get event by id '{}'", user.getPinedEventId());

        if (user.getPinedEventId() != 0) {
            event = eventService.getEvent(user.getPinedEventId());
        } else {
            log.debug("There is no pined event");
        }

        log.debug("setting user and event info to response entity");

        if (event != null) {
            user.setPinedEventDate(event.getEventDate());
            user.setPinedEventName(event.getName());
        }

        return user;
    }
}