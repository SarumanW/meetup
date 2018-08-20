package com.meetup.meetup.dao.rowMappers;


import com.meetup.meetup.entity.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static com.meetup.meetup.keys.Key.*;

@Component
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
        user.setPinedEventId(resultSet.getInt(UUSER_PINED_EVENT_ID));
        user.setPeriodicalEmail(resultSet.getString(UUSER_PERIODICAL_EMAIL));
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
