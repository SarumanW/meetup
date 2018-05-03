package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.rowMappers.UserRowMapper;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import static com.meetup.meetup.keys.Key.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@PropertySource("classpath:sqlDao.properties")
public class UserDaoImpl implements UserDao {

    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FolderDaoImpl folderDao;

    @Autowired
    private UserRowMapper userRowMapper;

    /**
     * Checks if login exists in the database
     *
     * @param login
     * @return true if login is free
     */
    @Override
    public boolean isLoginFree(String login) {
        log.debug("Try to check, if login '{}' is free", login);
        Integer numberOfUsers = jdbcTemplate.queryForObject(
                env.getProperty(USER_IS_LOGIN_FREE), new Object[]{login}, Integer.class
        );
        log.debug("Number of users with login '{}' is {}", login, numberOfUsers);
        return numberOfUsers == 0;
    }

    /**
     * Checks if login exists in the database
     *
     * @param email
     * @return true if login is free
     */
    @Override
    public boolean isEmailFree(String email) {
        log.debug("Try to check, if email '{}' is free", email);
        Integer numberOfUsers = jdbcTemplate.queryForObject(
                env.getProperty(USER_IS_EMAIL_FREE), new Object[]{email}, Integer.class
        );
        log.debug("Number of users with email '{}' is {}", email, numberOfUsers);
        return numberOfUsers == 0;
    }

    @Override
    public User findByLogin(String login) {
        log.debug("Try to find User by login: '{}'", login);
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(
                    env.getProperty(USER_FIND_BY_LOGIN),
                    new Object[]{login}, new UserRowMapper() {
                    }
            );
        } catch (DataAccessException e) {
            log.error("Query fails by finding user with login '{}'", login);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "User", "login", login));
        }
        log.debug("User with login '{}' was found", login);

        return user;

    }

    @Override
    public User findByEmail(String email) {
        log.debug("Try to find User by email: '{}'", email);
        User user = null;

        try {
            user = jdbcTemplate.queryForObject(
                    env.getProperty(USER_FIND_BY_EMAIL),
                    new Object[]{email}, new UserRowMapper() {
                    }
            );
        } catch (DataAccessException e) {
            log.error("Query fails by finding user with email '{}'", email);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "User", "email", email));
        }

        log.debug("User with email '{}' was found", email);
        return user;
    }

    @Override
    public List<User> getFriends(int userId) {
        log.debug("Try to getFriends by userId '{}'", userId);

        log.debug("Try to create list with FriendsIds by userId '{}'", userId);
        List<Integer> friendIds = getFriendsIds(userId);
        log.debug("List with FriendsIds by userId '{}' was created", userId);

        List<User> friends = new ArrayList<>();

        for (int id : friendIds) {

            log.debug("Try to find user entity by id '{}'", id);
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
        log.debug("Try to getFriendsIds with userId '{}'", userId);
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            list = jdbcTemplate.queryForList(env.getProperty(USER_GET_FRIENDS_IDS),
                    userId, userId);

        } catch (DataAccessException e) {
            log.error("Query fails by getFriendsIds by userId '{}'", userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (list.isEmpty()) {
            log.debug("list with friendsIds was not found by userId '{}'", userId);
        } else {
            log.debug("list with friendsIds was found by userId '{}'", userId);
        }

        BigDecimal b;
        List<Integer> friendIds = new ArrayList<>();

        for (Map<String, Object> row : list) {

            b = (BigDecimal) row.get(FRIEND_SENDER_ID);
            if (b == null) log.debug("Map contains no mapping for the key SENDER_ID");
            if (b.intValue() != userId) {
                log.debug("Try to add friend's id '{}' to friend list of user with id '{}'", b.intValue(), userId);
                friendIds.add(b.intValue());
            }

            b = (BigDecimal) row.get(FRIEND_RECEIVER_ID);
            if (b == null) log.debug("Map contains no mapping for the key RECEIVER_ID");
            if (b.intValue() != userId) {
                log.debug("Try to add friend's id '{}' to friend list of user with id '{}'", b.intValue(), userId);
                friendIds.add(b.intValue());
            }
        }

        return friendIds;
    }

    @Override
    public boolean addFriend(int senderId, int receiverId) {
        log.debug("Try to addFriend from '{}' to '{}'", senderId, receiverId);
        int result = 0;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_FRIEND);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(FRIEND_SENDER_ID, senderId);
        parameters.put(FRIEND_RECEIVER_ID, receiverId);
        parameters.put(FRIEND_IS_CONFIRMED, 0);

        try {
            result = simpleJdbcInsert.execute((parameters));

        } catch (DataAccessException e) {
            log.error("Query fails by addFriend from '{}' to '{}'", senderId, receiverId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (result != 0) {
            log.debug("addFriend from '{}' to '{}' successful", senderId, receiverId);
            return true;
        } else {
            log.debug("addFriend from '{}' to '{}' not successful", senderId, receiverId);
            return false;
        }

    }

    /**
     * Returns list of unconfirmed friend requests
     *
     * @param userId
     * @return
     */
    @Override
    public List<User> getFriendsRequests(int userId) {
        log.debug("Try to getUnconfirmedIds by userId '{}'", userId);
        List<Integer> friendIds = getUnconfirmedIds(userId);
        List<User> friends = new ArrayList<>();

        for (int id : friendIds) {
            log.debug("Try to get user entity by id '{}'", id);
            User friend = findById(id);
            if (friend != null) {
                log.debug("Try to add friend with id '{}' to user's '{}' friend list", friend.getId(), userId);
                friends.add(friend);
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
        log.debug("Try to getUnconfirmedIds by userId '{}'", userId);
        List<Integer> unConfirmedIds = new ArrayList<>();

        try {
            unConfirmedIds = jdbcTemplate.queryForList(env.getProperty(USER_GET_UNCONFIRMED_IDS),
                    new Object[]{userId}, Integer.class);

        } catch (DataAccessException e) {
            log.error("Query fails by finding unconfirmedIds with id '{}'", userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (unConfirmedIds.isEmpty()) {
            log.debug("unConfirmedIds not found by id '{}'", userId);
        } else {
            log.debug("unConfirmedIds were wound by id '{}'", userId);
        }
        return unConfirmedIds;
    }

    @Override
    public int confirmFriend(int userId, int friendId) {
        log.debug("Try to confirmFriend between '{}' and '{}'", userId, friendId);
        int result = 0;
        try {
            result = jdbcTemplate.update(env.getProperty(USER_CONFIRM_FRIEND), friendId, userId);

        } catch (DataAccessException e) {
            log.error("Query fails by confirmFriend between '{}' and '{}'", userId, friendId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (result != 0) {
            log.debug("Friendship confirm between '{}' and '{}'", userId, friendId);
        } else log.debug("Friendship not confirm between '{}' and '{}'", userId, friendId);
        return userId;
    }

    @Override
    public int deleteFriend(int userId, int friendId) {
        log.debug("Try to deleteFriend between '{}' and '{}'", userId, friendId);
        int result = 0;
        try {
            result = jdbcTemplate.update(env.getProperty(USER_DELETE_FRIEND), userId, friendId, friendId, userId);
        } catch (DataAccessException e) {
            log.error("Query fails by deleteFriend between '{}' and '{}'", userId, friendId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (result != 0) {
            log.debug("Friendship delete between '{}' and '{}'", userId, friendId);
        } else {
            log.debug("Friendship not delete between '{}' and '{}'", userId, friendId);
        }
        return userId;
    }

    @Override
    public User findById(int id) {
        log.debug("Try to find User by id: '{}'", id);
        User user = null;

        try {
            user = jdbcTemplate.queryForObject(
                    env.getProperty(USER_FIND_BY_ID),
                    new Object[]{id}, new UserRowMapper() {
                    }
            );

        } catch (DataAccessException e) {
            log.error("Query fails by finding user with id '{}'", id);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (user == null) {
            log.debug("User with id '{}' not found", id);
        } else {
            log.debug("User with id '{}' was found", id);
        }
        return user;
    }

    @Override
    public User insert(User model) {
        log.debug("Try to insert user with login '{}'", model.getLogin());
        int id = 0;
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_UUSER)
                .usingGeneratedKeyColumns(UUSER_USER_ID);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put(UUSER_USER_ID, model.getId());
        parameters.put(UUSER_LOGIN, model.getLogin());
        parameters.put(UUSER_PASSWORD, model.getPassword());
        parameters.put(UUSER_NAME, model.getName());
        parameters.put(UUSER_SURNAME, model.getLastname());
        parameters.put(UUSER_EMAIL, model.getEmail());
        parameters.put(UUSER_TIMEZONE, model.getTimeZone());
        parameters.put(UUSER_IMAGE_FILEPATH, model.getImgPath());
        parameters.put(UUSER_BDAY, (model.getBirthDay() != null ? Date.valueOf(model.getBirthDay()) : null));
        parameters.put(UUSER_PHONE, model.getPhone());
        try {
            log.debug("Try to execute statement");
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setId(id);

        } catch (DataAccessException e) {
            log.error("Query fails by insert User");
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (model.getId() != 0) {
            log.debug("user was added with id '{}'", id);
        } else {
            log.debug("user wasn't added with login '{}'", model.getLogin());
        }

        Folder folder = new Folder();
        folder.setName("general");
        folder.setUserId(id);
        log.debug("Try to insert general folder by folderDao");
        Folder folder2 = folderDao.insert(folder);
        log.debug("general folder was inserted with id '{}'", folder2.getFolderId());

        return model;
    }

    @Override
    public User update(User model) {
        log.debug("Try to update user with id '{}'", model.getId());
        int result = 0;
        try {
            result = jdbcTemplate.update(env.getProperty(USER_UPDATE),
                    model.getLogin(), model.getName(), model.getLastname(), model.getEmail(), model.getTimeZone(),
                    model.getImgPath(), Date.valueOf(model.getBirthDay()), model.getPhone(), model.getId());
        } catch (DataAccessException e) {
            log.error("Query fails by update user with id '{}'", model.getId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (result != 0) {
            log.debug("user with id'{}' was updated", model.getId());
        } else {
            log.debug("user with id'{}' was not updated", model.getId());
        }
        return model;
    }

    @Override
    public boolean updatePassword(User user) {
        log.debug("Try to update password, user with id '{}'", user.getId());
        int result = 0;
        try {
            result = jdbcTemplate.update(env.getProperty(USER_UPDATE_PASSWORD), user.getPassword(), user.getId());
        } catch (DataAccessException e) {
            log.error("Query fails by update user password with user id '{}'", user.getId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (result != 0) {
            log.debug("user with id '{}' update password", user.getId());
            return true;
        } else {
            log.debug("user with id '{}' not update password", user.getId());
            return false;
        }
    }

    @Override
    public User delete(User model) {
        log.debug("Try to delete user with id '{}'", model.getId());
        int result = 0;
        try {
            result = jdbcTemplate.update(env.getProperty(USER_DELETE), model.getId());
        } catch (DataAccessException e) {
            log.error("Query fails by delete user with id '{}'", model.getId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (result != 0) {
            log.debug("user with id '{}' was deleted", model.getId());
        } else {
            log.debug("user with id '{}' was not deleted", model.getId());
        }
        return model;
    }

    /**
     * Actual method of searching unknown users for specific user.
     * @param userId    id of specific user.
     * @param userName  username pattern of unknown users
     * @return
     */
    @Override
    public List<User> getNotFriends(int userId, String userName) {

        List<Map<String, Object>> userParamsList;

        userParamsList = jdbcTemplate.queryForList(env.getProperty("user.getNotFriends"),userId,userId,userName+"%");

        return userRowMapper.mapRow(userParamsList);

    }
}