package com.meetup.meetup.dao;

/**
 * Dao interface for User entity.
 *
 * @param <User>
 */
public interface UserDao<User> extends Dao<User> {

    User findByLogin(String login);
    User findByEmail(String email);

}
