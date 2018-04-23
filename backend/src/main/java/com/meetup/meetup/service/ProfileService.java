package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import com.meetup.meetup.entity.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public boolean updateUser(User newUser) {
        User updatedUser = userDao.findById(newUser.getId()).setState(newUser);
        if(userDao.update(updatedUser) != -1){
            return true;
        }
        return false;
    }

//    TODO method require testing!

    public List<Friend> getFriends() {
        User user = authenticationFacade.getAuthentication();

        List<Integer> IDs = userDao.getFriendsIds(user.getId());

        List<Friend> friends = new ArrayList<>();

        IDs.forEach((id) -> new Friend(true,userDao.findById(id)));

        return friends;
    }
}