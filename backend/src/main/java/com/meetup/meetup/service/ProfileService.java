package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.ProfileNotFoundException;
import com.meetup.meetup.service.vm.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    public Profile getProfile(String login) {
        Profile profile = new Profile(userDao.findByLogin(login));
        if (profile != null) {
            return profile;
        }
        throw new ProfileNotFoundException(login);
    }

    // TODO: 20.04.2018 logic with -1
    public boolean updateProfile(Profile updatedProfile) {
        User updatedUser = userDao.findById(updatedProfile.getId());
        if (updatedUser != null) {
            User newUser = updatedProfile.getUser(updatedUser);
            if (userDao.update(newUser) != -1) {
                return true;
            }
            return false;
        } else {
            throw new ProfileNotFoundException(updatedProfile.getId());
        }
    }


    public List<Profile> getFriends(int id) {
        List<Integer> listFriendsIds = userDao.friendsIds(id);
        List<Profile> listFriendsProfiles = new ArrayList<>();
        listFriendsIds.forEach((friendId) -> listFriendsProfiles.add(new Profile(userDao.findById(friendId))));
        return listFriendsProfiles;
    }

    public Profile getProfileFromToken(String token) {
        try {
            return jwtService.verify(token);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}