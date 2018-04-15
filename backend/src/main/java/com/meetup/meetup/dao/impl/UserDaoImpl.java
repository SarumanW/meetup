package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class UserDaoImpl implements UserDao<User> {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public String testMethod(){
        return "configuration passed";
    }

    @Autowired
    @Qualifier("jdbcTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User findByLogin(String login) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public int save(User model) {
        return 0;
    }

    @Override
    public int update(User model) {
        return 0;
    }

    @Override
    public int delete(User model) {
        return 0;
    }
}
