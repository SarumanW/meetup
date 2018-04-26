package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.rowMappers.UserRowMapper;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
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

    @Autowired
    private FolderDaoImpl folderDao;

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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }

        return user;
    }

    @Override
    public List<User> getFriends(int userId) {

        List<Integer> friendIds = getFriendsIds(userId);
        List<User> friends = new ArrayList<>();

        for (int id : friendIds) {
            User friend = findById(id);

            if (friend != null) {
                friends.add(friend);
            }
        }

        return friends;
    }


    /**
     * Returns list of user ids where friendship for given user's id is confirmed
     *
     * @param userId
     * @return
     */
    private List<Integer> getFriendsIds(int userId) {

        List<Map<String, Object>> list = new ArrayList<>();

        try {
            list = jdbcTemplate.queryForList(env.getProperty("user.getFriendsIds"),
                userId, userId);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }


        BigDecimal b;
        List<Integer> friendIds = new ArrayList<>();

        for (Map<String, Object> row : list) {
            b = (BigDecimal) row.get("SENDER_ID");
            if (b.intValue() != userId) {
                friendIds.add(b.intValue());
            }

            b = (BigDecimal) row.get("RECEIVER_ID");
            if (b.intValue() != userId) {
                friendIds.add(b.intValue());
            }
        }

        return friendIds;
    }


    @Override
    public boolean addFriend(int senderId, int receiverId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("FRIEND");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SENDER_ID", senderId);
        parameters.put("RECEIVER_ID", receiverId);
        parameters.put("IS_CONFIRMED", 0);

        try {
            simpleJdbcInsert.execute((parameters));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Returns list of unconfirmed friend requests
     *
     * @param userId
     * @return
     */
    @Override
    public List<User> getFriendsRequests(int userId) {

        List<Integer> friendIds = getUnconfirmedIds(userId);
        List<User> friends = new ArrayList<>();

        for (int id : friendIds) {
            User friend = findById(id);
            if (friend != null) {
                friends.add(findById(id));
            }
        }

        return friends;
    }


    /**
     * Returns list of ids where friendship isn't confirmed for given user's id
     *
     * @param userId
     * @return
     */
    private List<Integer> getUnconfirmedIds(int userId) {

        List<Integer> unConfirmedIds = new ArrayList<>();

        try {
            unConfirmedIds = jdbcTemplate.queryForList(env.getProperty("user.getUnconfirmedIds"),
                new Object[] {userId},Integer.class);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return unConfirmedIds;
    }

    //working
    @Override
    public int confirmFriend(int userId, int friendId) {
        try {
            jdbcTemplate.update(env.getProperty("user.confirmFriend"), friendId, userId);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return userId;
    }

    //working
    @Override
    public int deleteFriend(int userId, int friendId) {
        try {
            jdbcTemplate.update(env.getProperty("user.deleteFriend"), userId, friendId, friendId, userId);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return userId;
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
            System.out.println(e.getMessage());
        }

        return user;
    }

    @Override
    public User insert(User model) {

        int id = -1;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("UUSER")
            .usingGeneratedKeyColumns("USER_ID");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("USER_ID", model.getId());
        parameters.put("login", model.getLogin());
        parameters.put("password", model.getPassword());
        parameters.put("name", model.getName());
        parameters.put("surname", model.getLastname());
        parameters.put("email", model.getEmail());
        parameters.put("timezone", model.getTimeZone());
        parameters.put("image_filepath", model.getImgPath());
        parameters.put("bday", (model.getBirthDay() != null ? Date.valueOf(model.getBirthDay()) : null));
        parameters.put("phone", model.getPhone());

        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setId(id);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        Folder folder = new Folder();
        folder.setName("general");
        folder.setUserId(id);

        folderDao.insert(folder);

        return model;
    }

    @Override
    public User update(User model) {
        try {
            jdbcTemplate.update(env.getProperty("user.update"),
                model.getLogin(), model.getName(), model.getLastname(), model.getEmail(), model.getTimeZone(),
                model.getImgPath(), Date.valueOf(model.getBirthDay()), model.getPhone(), model.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return model;
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
    public User delete(User model) {
        try {
            jdbcTemplate.update(env.getProperty("user.delete"), model.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return model;
    }
}