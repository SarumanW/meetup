package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.FolderDao;
import com.meetup.meetup.dao.rowMappers.FolderRowMapper;
import com.meetup.meetup.entity.*;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import static com.meetup.meetup.keys.Key.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@PropertySource("classpath:sqlDao.properties")
@PropertySource("classpath:strings.properties")
public class FolderDaoImpl implements FolderDao {

    private static Logger log = LoggerFactory.getLogger(FolderDaoImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Folder> getUserFolders(int id) {
        log.debug("Try to get folders for user with id '{}'", id);
        List<Folder> userFolders = new ArrayList<>();

        try {
            userFolders = jdbcTemplate.query(env.getProperty(FOLDER_GET_USER_FOLDERS),
                    new Object[]{id}, new FolderRowMapper());
        } catch (EmptyResultDataAccessException e) {
            log.debug("Folders with userId '{}' was not found", id);
            return userFolders;
        } catch (DataAccessException e) {
            log.error("Query fails by getting folders for user with id '{}'", id);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Users folders for user with id '{}' were founded counted '{}' pcs", id, userFolders.size());

        return userFolders;
    }

    @Override
    public Folder findById(int id) {
        log.debug("Try to find folder with id '{}'", id);
        Folder folder;

        try {
            folder = jdbcTemplate.queryForObject(
                    env.getProperty(FOLDER_GET_BY_ID),
                    new Object[]{id}, new FolderRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            log.debug("Folder with folderId '{}' was not found", id);
            return null;
        } catch (DataAccessException e) {
            log.error("Query fails by finding folder with id '{}'", id);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Folder with id '{}' founded", id);

        return folder;
    }

    @Override
    public Folder findByName(String name) {
        log.debug("Try to find folder with name '{}'", name);
        Folder folder;

        try {
            folder = jdbcTemplate.queryForObject(
                    env.getProperty(FOLDER_GET_BY_NAME),
                    new Object[]{name}, new FolderRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            log.debug("Folder with name '{}'  was not found", name);
            return null;
        } catch (DataAccessException e) {
            log.error("Query fails by finding folder with name '{}'", name);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Folder with name '{}' found", name);

        return folder;
    }

    @Override
    public boolean moveEventsToGeneral(int id) {
        log.debug("Try to move events to general with id '{}'", id);
        int result = 0;
        try {
            result = jdbcTemplate.update(env.getProperty(FOLDER_REMOVE_EVENTS), id);

        } catch (DataAccessException e) {
            log.error("Query fails by moving events to general with id '{}'", id);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        boolean isSuccessful = result == 0;

        log.debug("Moving events to general with id '{}' was {}", id, isSuccessful ? "successful" : "failed");

        return isSuccessful;
    }

    @Override
    public Folder findById(int id, int userId) {
        log.debug("Try to find folder with id '{}' for user with id '{}'", id, userId);
        Folder folder;

        try {
            folder = jdbcTemplate.queryForObject(
                    env.getProperty(FOLDER_GET_BY_ID),
                    new Object[]{id, userId}, new FolderRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            log.debug("Folder with folderId '{}' for user with userId '{}' was not found", id, userId);
            return null;
        } catch (DataAccessException e) {
            log.error("Query fails by finding folder with id '{}' for user with id '{}'", id, userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Folder with id '{}' for user with id '{}' was found", id, userId);

        return folder;
    }

    @Override
    public Folder insert(Folder model) {
        log.debug("Try to insert folder with name '{}' by user with id '{}'", model.getName(), model.getUserId());
        int id;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_FOLDER)
                .usingGeneratedKeyColumns(FOLDER_FOLDER_ID);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(FOLDER_NAME, model.getName());
        parameters.put(FOLDER_USER_ID, model.getUserId());

        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setFolderId(id);
        } catch (DataAccessException e) {
            log.error("Query fails by inserting folder with name '{}' by user with id '{}'", model.getName(), model.getUserId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (model.getFolderId() != 0) {
            log.debug("Inserting folder with name '{}' by user with id '{}' successful folder id '{}'", model.getName(), model.getUserId(), model.getFolderId());
        }

        return model;
    }

    @Override
    public Folder update(Folder model) {
        log.debug("Try to update folder with id '{}'", model.getFolderId());
        int result = 0;
        try {
            result = jdbcTemplate.update(env.getProperty(FOLDER_UPDATE),
                    model.getName(), model.getFolderId());
        } catch (DataAccessException e) {
            log.error("Query fails by updating folder with id '{}'", model.getFolderId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Updating folder with id '{}' {}", model.getFolderId(), result == 0 ? "failed" : "successful");

        return model;
    }

    @Override
    public Folder delete(Folder model) {
        log.debug("Try to delete folder with id '{}'", model.getFolderId());
        int result = 0;
        try {
            result = jdbcTemplate.update(env.getProperty(FOLDER_DELETE), model.getFolderId());
        } catch (DataAccessException e) {
            log.error("Query fails by deleting folder with id '{}'", model.getFolderId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("Deleting folder with id '{}' {}", model.getFolderId(), result == 0 ? "failed" : "successful");

        return model;
    }
}
