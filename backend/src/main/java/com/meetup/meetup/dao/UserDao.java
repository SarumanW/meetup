package com.meetup.meetup.dao;

import com.meetup.meetup.entity.User;

import java.util.List;

public interface UserDao extends Dao<User> {

    User findByLogin(String login);

    User findByEmail(String email);

    boolean updatePassword(User user);

    int confirmFriend(int userId, int friendId);

    int deleteFriend(int userId, int friendId);

    List<User> getFriendsRequests(int userId);

    List<User> getFriends(int userId);

    boolean addFriend(int senderId, int receiverId);

}
