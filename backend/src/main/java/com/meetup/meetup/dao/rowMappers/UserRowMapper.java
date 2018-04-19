package com.meetup.meetup.dao.rowMappers;


import com.meetup.meetup.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        Date bday;

        user.setId(resultSet.getInt("USER_ID"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setName(resultSet.getString("name"));
        user.setLastname(resultSet.getString("surname"));
        user.setBirthDay("bday");
        user.setTimeZone(resultSet.getInt("timezone"));
        user.setImgPath(resultSet.getString("image_filepath"));

        return user;
    }
}
