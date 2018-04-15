package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.rowMappers.UserRowMapper;
import com.meetup.meetup.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;


@Repository
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
        User user = null;

        try {
            user = jdbcTemplate.queryForObject(
                    "SELECT USER_ID,login, password, name, surname, email, timezone, image_filepath, bday, phone " +
                            "FROM USER_S WHERE LOGIN = ?",
                    new Object[]{login}, new UserRowMapper() {
                    }
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User findByEmail(String email) {

        User user = null;

        try {
            user = jdbcTemplate.queryForObject(
                    "SELECT USER_ID,login, password, name, surname, email, timezone, image_filepath, bday, phone " +
                            "FROM USER_S WHERE EMAIL = ?",
                    new Object[]{email}, new UserRowMapper() {
                    }
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User findById(int id) {
        User user = null;

        try {
            user = jdbcTemplate.queryForObject(
                    "SELECT USER_ID,login, password, name, surname, email, timezone, image_filepath, bday, phone " +
                            "FROM USER_S WHERE USER_ID = ?",
                    new Object[]{id}, new UserRowMapper() {
                    }
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public int insert(User model) {

        int id = -1;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("USER_S")
                .usingGeneratedKeyColumns("USER_ID");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("USER_ID", model.getId());
        parameters.put("login", model.getLogin());
        parameters.put("password", model.getPassword());
        parameters.put("name", model.getName());
        parameters.put("surname", model.getLastName());
        parameters.put("email", model.getEmail());
        parameters.put("timezone", model.getTimeZone());
        parameters.put("image_filepath", model.getImgPath());
        parameters.put("bday", Date.valueOf(model.getBirthDay()));
        parameters.put("phone", model.getPhone());

        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setId(id);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        return id;
    }

    @Override
    public int update(User model) {
        try {
            jdbcTemplate.update("UPDATE USER_S SET LOGIN = ?, PASSWORD = ?, NAME = ?, SURNAME = ?, EMAIL = ?," +
                            "TIMEZONE = ?, IMAGE_FILEPATH = ?, BDAY = ?, PHONE = ? WHERE USER_ID = ?",
                    model.getId(), model.getPassword(),  model.getName(), model.getLastName(), model.getEmail(), model.getTimeZone(),
                    model.getImgPath(), Date.valueOf(model.getBirthDay()), model.getPhone(), model.getId());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return model.getId();
    }

    @Override
    public int delete(User model) {
        try {
            jdbcTemplate.update("DELETE FROM USER_S WHERE USER_ID = ?", model.getId());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return model.getId();
    }
}
