package com.meetup.meetup.dao.rowMappers;


import com.meetup.meetup.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.meetup.meetup.Keys.Key.*;


public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        Date date;

        user.setId(resultSet.getInt(UUSER_USER_ID));
        user.setLogin(resultSet.getString(UUSER_LOGIN));
        user.setPassword(resultSet.getString(UUSER_PASSWORD));
        user.setEmail(resultSet.getString(UUSER_EMAIL));
        user.setPhone(resultSet.getString(UUSER_PHONE));
        user.setName(resultSet.getString(UUSER_NAME));
        user.setLastname(resultSet.getString(UUSER_SURNAME));
        date = resultSet.getDate(UUSER_BDAY);
        user.setBirthDay(date == null ? null : date.toString());
        user.setTimeZone(resultSet.getInt(UUSER_TIMEZONE));
        user.setImgPath(resultSet.getString(UUSER_IMAGE_FILEPATH));

        return user;
    }
}
