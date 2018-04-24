package com.meetup.meetup.dao;

import com.meetup.meetup.entity.User;

import java.util.List;

public interface UserDao extends Dao<User> {

    User findByLogin(String login);

    User findByEmail(String email);

    boolean updatePassword(User user);

    int confirmFriend(int user_id, int friend_id);

    int deleteFriend(int user_id, int friend_id);

    List<User> getFriendsRequests(int user_id);

    List<User> getFriends(int user_id);

    boolean addFriend(int senderId, int receiverId);

}
