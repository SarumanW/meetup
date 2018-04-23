package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.rowMappers.UserRowMapper;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@PropertySource("classpath:sqlDao.properties")
public class UserDaoImpl implements UserDao {

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public User findByLogin(String login) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(
                    env.getProperty("user.findByLogin"),
                    new Object[]{login}, new UserRowMapper() {
                    }
            );
        } catch (DataAccessException e) {
            System.out.println(new UserNotFoundException("login", login).getMessage());
        }

        return user;
    }

    @Override
    public User findByEmail(String email) {

        User user = null;
        try {
            user = jdbcTemplate.queryForObject(
                    env.getProperty("user.findByEmail"),
                    new Object[]{email}, new UserRowMapper() {
                    }
            );
        } catch (DataAccessException e) {
            System.out.println(new UserNotFoundException("email", email).getMessage());
        }

        return user;
    }

    @Override
    public List<Integer> getFriendsIds(int id) {

        List<Integer> resultList;

        resultList = jdbcTemplate.queryForList(env.getProperty("user.getFriendsIds"), new Object[]{id}, Integer.class);

        return resultList;

    }


    @Override
    public User findById(int id) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(
                    env.getProperty("user.findById"),
                    new Object[]{id}, new UserRowMapper() {
                    }
            );
        } catch (DataAccessException e) {
            System.out.println(new UserNotFoundException("id", id+"").getMessage());
        }
        return user;
    }

    @Override
    public int insert(User model) {

        int id = -1;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("uuser")
                .usingGeneratedKeyColumns("USER_ID");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("USER_ID", model.getId());
        parameters.put("login", model.getLogin());
        parameters.put("password", model.getPassword());
        parameters.put("name", model.getName());
        parameters.put("surname", model.getLastname());
        parameters.put("email", model.getEmail());
        parameters.put("time_zone", model.getTimeZone());
        parameters.put("image_filepath", model.getImgPath());
        parameters.put("birthday", (model.getBirthDay() != null ? Date.valueOf(model.getBirthDay()) : null));
        parameters.put("phone", model.getPhone());
        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setId(id);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    @Override
    public int update(User model) {
        try {
            jdbcTemplate.update(env.getProperty("user.update"),
                    model.getLogin(), model.getPassword(), model.getName(), model.getLastname(), model.getEmail(), model.getTimeZone(),
                    model.getImgPath(), Date.valueOf(model.getBirthDay()), model.getPhone(), model.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return model.getId();
    }

    @Override
    public boolean updatePassword(User user) {
        try {
            jdbcTemplate.update(env.getProperty("user.updatePassword"), user.getPassword(), user.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public int delete(User model) {
        try {
            jdbcTemplate.update(env.getProperty("user.delete"), model.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return model.getId();
    }
}
