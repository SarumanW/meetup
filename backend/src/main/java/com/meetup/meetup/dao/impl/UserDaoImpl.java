package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.AbstractDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.rowMappers.UserRowMapper;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
import com.meetup.meetup.exception.runtime.DeleteException;
import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import com.meetup.meetup.exception.runtime.UpdateException;
import com.meetup.meetup.exception.runtime.frontend.detailed.RequestAlreadySentException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meetup.meetup.keys.Key.*;


@Repository
@PropertySource("classpath:sqlDao.properties")
public class UserDaoImpl extends AbstractDao<User> implements UserDao {


    @Autowired
    private FolderDaoImpl folderDao;

    @Autowired
    private UserRowMapper userRowMapper;

    public UserDaoImpl() {
        log = LoggerFactory.getLogger(UserDaoImpl.class);
    }

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
    public List<User> getByEmailPeriod(String period) {
        log.debug("Try to users by email period '{}'", period);

        List<User> users = jdbcTemplate.query(env.getProperty(USER_GET_BY_EMAIL_PERIOD), new Object[]{period}, new UserRowMapper());
        log.debug("Users found: '{}'", users);

        return users;
    }

    @Override
    public User findByLogin(String login) {
        log.debug("Try to find User by login: '{}'", login);
        List<User> users;
        try {
            users = jdbcTemplate.query(
                    env.getProperty(USER_FIND_BY_LOGIN),
                    new Object[]{login}, new UserRowMapper() {
                    }
            );
            if(users.isEmpty()){
                return null;
            }
        } catch (EmptyResultDataAccessException e) {
            log.error("User was not found by  login '{}'", login);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "User", "login", login));
        } catch (DataAccessException e) {
            log.error("Query fails by finding user with login '{}'", login,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("User with login '{}' was found", login);

        return users.get(0);
    }

    @Override
    public User findByEmail(String email) {
        log.debug("Try to find User by email: '{}'", email);
        List<User> users;

        try {
            users = jdbcTemplate.query(
                    env.getProperty(USER_FIND_BY_EMAIL),
                    new Object[]{email}, new UserRowMapper() {
                    }
            );
            if(users.isEmpty()){
                return null;
            }
        } catch (DataAccessException e) {
            log.error("Query fails by finding user with email '{}'", email,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("User with email '{}' was found", email);
        return users.get(0);
    }

    @Override
    public List<User> getFriends(int userId) {
        log.debug("Try to getFriends by userId '{}'", userId);

        List<User> friends = jdbcTemplate.query(env.getProperty(USER_GET_FRIENDS), new Object[]{userId, userId}, new UserRowMapper());
        log.debug("Friends found: '{}'", friends);

        return friends;
    }


    @Override
    public boolean addFriend(int senderId, int receiverId) {
        log.debug("Try to addFriend from '{}' to '{}'", senderId, receiverId);
        int result;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_FRIEND);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(FRIEND_SENDER_ID, senderId);
        parameters.put(FRIEND_RECEIVER_ID, receiverId);
        parameters.put(FRIEND_IS_CONFIRMED, 0);

        try {
            result = simpleJdbcInsert.execute((parameters));

        } catch (DuplicateKeyException e) {
            log.error("Request from '{}' to '{}' already exists", senderId, receiverId,e);
            throw new RequestAlreadySentException(env.getProperty(EXCEPTION_REQUEST_ALREADY_SENT));
        } catch (DataAccessException e) {
            log.error("Query fails by addFriend from '{}' to '{}'", senderId, receiverId,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (result != 0) {
            log.debug("addFriend from '{}' to '{}' successful", senderId, receiverId);
            return true;
        } else {
            log.error("addFriend from '{}' to '{}' not successful", senderId, receiverId);
            return false;
        }

    }


    @Override
    public List<User> getFriendsRequests(int userId) {
        log.debug("Try to getUnconfirmedIds by userId '{}'", userId);

        List<User> friendsRequests = jdbcTemplate.query(env.getProperty(USER_GET_UNCONFIRMED), new Object[]{userId}, new UserRowMapper());
        log.debug("Friends request found '{}'", friendsRequests);

        return friendsRequests;
    }


    @Override
    public int confirmFriend(int userId, int friendId) {
        log.debug("Try to confirmFriend between '{}' and '{}'", userId, friendId);
        int result;
        try {
            result = jdbcTemplate.update(env.getProperty(USER_CONFIRM_FRIEND), friendId, userId);

        } catch (DataAccessException e) {
            log.error("Query fails by confirmFriend between '{}' and '{}'", userId, friendId,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (result != 0) {
            log.debug("Friendship confirm between '{}' and '{}'", userId, friendId);
        } else {
            log.error("Friendship not confirm between '{}' and '{}'", userId, friendId);
            throw new UpdateException(env.getProperty(EXCEPTION_UPDATE));
        }
        return result;
    }

    @Override
    public int deleteFriend(int userId, int friendId) {
        log.debug("Try to deleteFriend between '{}' and '{}'", userId, friendId);
        int result;
        try {
            result = jdbcTemplate.update(env.getProperty(USER_DELETE_FRIEND), userId, friendId, friendId, userId);
        } catch (DataAccessException e) {
            log.error("Query fails by deleteFriend between '{}' and '{}'", userId, friendId,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (result != 0) {
            log.debug("Friendship delete between '{}' and '{}'", userId, friendId);
        } else {
            log.error("Friendship not delete between '{}' and '{}'", userId, friendId);
            throw new DeleteException(EXCEPTION_DELETE);
        }
        return result;
    }

    @Override
    public User findById(int id) {
        log.debug("Try to find User by id: '{}'", id);
        User user;

        try {
            user = jdbcTemplate.queryForObject(
                    env.getProperty(USER_FIND_BY_ID),
                    new Object[]{id}, new UserRowMapper() {
                    }
            );

        } catch (EmptyResultDataAccessException e) {
            log.error("User with id '{}' not found", id,e);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "User", "id", id));
        }catch (DataAccessException e) {
            log.error("Query fails by finding user with id '{}'", id,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("User with id '{}' was found", id);

        return user;
    }

    @Override
    public User insert(User model) {
        log.debug("Try to insert user with login '{}'", model.getLogin());

        int id;

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
        parameters.put(UUSER_REGISTER_DATE, model.getRegisterDate());

        try {
            log.debug("Try to execute statement");
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setId(id);
        } catch (DataAccessException e) {
            log.error("Query fails by insert User",e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        Folder folder = new Folder();
        folder.setName("general");
        folder.setUserId(id);

        log.debug("Try to insert general folder by folderDao");

        int generalFolderId = folderDao.insert(folder).getFolderId();

        log.debug("General folder was inserted with id '{}'", generalFolderId);

        return model;
    }

    @Override
    public User update(User model) {
        log.debug("Try to update user with id '{}'", model.getId());
        int result;
        try {
            result = jdbcTemplate.update(env.getProperty(USER_UPDATE),
                    model.getLogin(), model.getName(), model.getLastname(), model.getEmail(), model.getTimeZone(),
                    model.getImgPath(), (model.getBirthDay() == null ? null : Date.valueOf(model.getBirthDay())), model.getPhone(), model.getPeriodicalEmail(), model.getId());
        } catch (DataAccessException e) {
            log.error("Query fails by update user with id '{}'", model.getId(),e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (result != 0) {
            log.debug("user with id'{}' was updated", model.getId());
        } else {
            log.error("user with id'{}' was not updated", model.getId());
            throw new UpdateException(env.getProperty(EXCEPTION_UPDATE));
        }
        return model;
    }

    @Override
    public void updatePassword(User user) {
        log.debug("Try to update password, user with id '{}'", user.getId());

        int result;

        try {
            result = jdbcTemplate.update(env.getProperty(USER_UPDATE_PASSWORD), user.getPassword(), user.getId());
        } catch (DataAccessException e) {
            log.error("Query fails by update user password with user id '{}'", user.getId(),e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (result != 0) {
            log.debug("user with id '{}' update password", user.getId());
        } else {
            log.error("user with id '{}' not update password", user.getId());
            throw new UpdateException(env.getProperty(EXCEPTION_UPDATE));
        }
    }

    @Override
    public User delete(User model) {
        log.debug("Try to delete user with id '{}'", model.getId());
        int result;
        try {
            result = jdbcTemplate.update(env.getProperty(USER_DELETE), model.getId());
        } catch (DataAccessException e) {
            log.error("Query fails by delete user with id '{}'", model.getId(),e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (result != 0) {
            log.debug("user with id '{}' was deleted", model.getId());
        } else {
            log.error("user with id '{}' was not deleted", model.getId());
            throw new DeleteException(EXCEPTION_DELETE);
        }
        return model;
    }

    @Override
    public List<User> getAllByUsernamePart(String userName) {

        log.debug("Try to get user by parametrs by username '{}'", userName);

        List<Map<String, Object>> userParamsList;

        try {
            userParamsList = jdbcTemplate.queryForList(env.getProperty(USER_GET_ALL_BY_PART), userName + "%");
        } catch (DataAccessException e) {
            log.error("Query fails by delete user with id '{}'", userName,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        return userRowMapper.mapRow(userParamsList);

    }

    @Override
    public List<User> getFriendsByUsernamePart(int userId, String userName) {

        log.debug("Try to get friends by user id '{}' and username '{}'", userId, userName);
        List<Map<String, Object>> userParamsList;

        try {
            userParamsList = jdbcTemplate.queryForList(env.getProperty(USER_GET_FRIENDS_BY_USERNAME_PART), userId, userId, userName + "%");
        } catch (DataAccessException e) {
            log.error("Query fails by delete user with id '{}'", userName,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        return userRowMapper.mapRow(userParamsList);

    }

    /**
     * Actual method of searching unknown users for specific user.
     *
     * @param userId   id of specific user.
     * @param userName username pattern of unknown users
     * @return List<User>
     */
    @Override
    public List<User> getPotentialFriendsByUsernamePart(int userId, String userName) {

        log.debug("Try to get not friends by user id '{}' and username '{}'", userId, userName);

        List<Map<String, Object>> userParamsList;

        try {
            userParamsList = jdbcTemplate.queryForList(env.getProperty(USER_GET_NOT_FRIENDS_BY_USERNAME_PART), userId, userId, userName + "%");
        } catch (DataAccessException e) {
            log.error("Query fails by delete user with id '{}'", userName,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        return userRowMapper.mapRow(userParamsList);

    }

    @Override
    public int deleteUnconfirmedAccounts() {
        log.debug("Try to delete unconfirmed accounts");

        int result;

        try {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            result = jdbcTemplate.update(env.getProperty(USER_DELETE_UNCONFIRMED_ACCOUNTS), currentTimestamp);
        } catch (DataAccessException e) {
            log.error("Query fails by delete unconfirmed accounts",e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Deleted {} unconfirmed accounts", result);

        return result;
    }

    @Override
    public String findLoginById(int id) {
        String login;
        try {
             login = jdbcTemplate.queryForObject(
                    env.getProperty(USER_GET_LOGIN_BY_ID),
                    new Object[]{id}, String.class);
        } catch (EmptyResultDataAccessException e) {
            log.error("Login with id '{}' not found", id,e);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "Login", "id", id));
        } catch (DataAccessException e) {
            log.error("Query fails by finding user with id '{}'", id,e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        return login;
    }
}