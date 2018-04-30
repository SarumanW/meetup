package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.rowMappers.UserRowMapper;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@PropertySource("classpath:sqlDao.properties")
public class UserDaoImpl implements UserDao {

    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final Environment env;
    private final JdbcTemplate jdbcTemplate;
    private final FolderDaoImpl folderDao;

    @Autowired
    public UserDaoImpl(Environment env, JdbcTemplate jdbcTemplate, FolderDaoImpl folderDao) {
        this.env = env;
        this.jdbcTemplate = jdbcTemplate;
        this.folderDao = folderDao;
    }

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

    // TODO for further improvements of user search
    /*@Override
    public List<User> findByParams(String login, String name, String surname, Integer limit) {

        List<User> users = null;

        login = (login == null) ? "" : login;
        name = (name == null) ? "" : name;
        surname = (surname == null) ? "" : surname;
        String limitStr = (limit == null) ? env.getProperty("user.limitSearchConst") : limit.toString();

        log.debug("Trying to get users by params " +
                "login {}, " +
                "name {}, " +
                "surname {}, " +
                "limit '{} " +
                "from database",
                '\'' + login + "%'",
                '\'' + name + "%'",
                '\'' + surname + "%'",
                limitStr);

        try {
            List<Map<String, Object>> userParamsList = jdbcTemplate.queryForList(
                    env.getProperty("user.findByParams"),
                    login + "%",
                    name + "%",
                    surname + "%",
                    limitStr);

            log.debug("Trying to convert List<Map<String, Object>> to List<User>");

            users = new UserRowMapper().mapRow(userParamsList);
        } catch (DataAccessException e) {
            log.error("Data access error");
            e.printStackTrace();
        }

        log.debug("Found users '{}' in database", users);

        return users;
    }*/

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
     * Actual method of searching unknown users for specific user.
     * @param userId    id of specific user.
     * @param userName  username pattern of unknown users
     * @return
     */
    @Override
    public List<User> getNotFriends(int userId, String userName) {

        List<User> users;

        users = jdbcTemplate.queryForList(env.getProperty("user.getNotFriends"),new Object[] {userId,userId,userName+"%"}, User.class);

        return users;
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


        Integer b;
        List<Integer> friendIds = new ArrayList<>();

        for (Map<String, Object> row : list) {
            b = (Integer) row.get("SENDER_ID");
            if (b != userId) {
                friendIds.add(b);
            }

            b = (Integer) row.get("RECEIVER_ID");
            if (b != userId) {
                friendIds.add(b);
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
                model.getLogin(), model.getPassword(), model.getName(), model.getLastname(), model.getEmail(), model.getTimeZone(),
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