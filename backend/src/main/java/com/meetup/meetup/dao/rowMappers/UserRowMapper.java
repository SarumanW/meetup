package com.meetup.meetup.dao.rowMappers;


import com.meetup.meetup.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        Date date;

        user.setId(resultSet.getInt("USER_ID"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setName(resultSet.getString("name"));
        user.setLastname(resultSet.getString("surname"));
        date = resultSet.getDate("bday");
        user.setBirthDay(date == null ? null : date.toString());
        user.setTimeZone(resultSet.getInt("timezone"));
        user.setImgPath(resultSet.getString("image_filepath"));

        return user;
    }

    public List<User> mapRow(List<Map<String, Object>> userParamsList) {
        List<User> users = new ArrayList<>();
        for (Map<String, Object> userParams : userParamsList) {
            User user = new User();

            user.setId(((BigDecimal) userParams.get("USER_ID")).intValue());
            user.setLogin(userParams.get("login").toString());
            user.setPassword((String) userParams.get("password"));
            user.setEmail((String) userParams.get("email"));
            user.setPhone((String) userParams.get("phone"));
            user.setName((String) userParams.get("name"));
            user.setLastname((String) userParams.get("surname"));
            Timestamp birthDay = (Timestamp) userParams.get("bday");
            user.setBirthDay(birthDay == null ? null : birthDay.toString());
            user.setTimeZone(((BigDecimal) userParams.get("timezone")).intValue());
            user.setImgPath((String) userParams.get("image_filepath"));

            users.add(user);
        }
        return users;
    }
}
