package com.meetup.meetup.dao;

import com.meetup.meetup.entity.User;

import java.util.List;

public interface UserDao extends Dao<User> {

    User findByLogin(String login);

    User findByEmail(String email);

    User update(User model);

    void updatePassword(User user);

    int confirmFriend(int userId, int friendId);

    int deleteFriend(int userId, int friendId);

    List<User> getFriendsRequests(int userId);

    List<User> getFriends(int userId);

    List<User> getAllByUsernamePart(String userName);

    List<User> getFriendsByUsernamePart(int userId, String userName);

    List<User> getPotentialFriendsByUsernamePart(int userId, String userName);

    boolean addFriend(int senderId, int receiverId);

    boolean isLoginFree(String login);

    boolean isEmailFree(String email);

    List<User> getByEmailPeriod(String period);

    int deleteUnconfirmedAccounts();

    String findLoginById(int id);

}
