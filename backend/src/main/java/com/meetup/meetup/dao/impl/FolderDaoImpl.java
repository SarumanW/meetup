package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.FolderDao;
import com.meetup.meetup.dao.rowMappers.EventRowMapper;
import com.meetup.meetup.dao.rowMappers.FolderRowMapper;
import com.meetup.meetup.entity.*;
import com.meetup.meetup.exception.DatabaseWorkException;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import static com.meetup.meetup.Keys.Key.*;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@PropertySource("classpath:sqlDao.properties")
public class FolderDaoImpl implements FolderDao {

    private static Logger log = LoggerFactory.getLogger(FolderDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Folder> getUserFolders(int id) {
        log.debug("Try to get folders for user with id '{}'", id);
        List<Folder> userFolders = new ArrayList<>();

        try {
            userFolders = jdbcTemplate.query(GET_USER_FOLDERS,
                    new Object[]{id}, new FolderRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by getting folders for user with id '{}'", id);
            System.out.println(e.getMessage());
        }
        if (userFolders.isEmpty()) {
            log.debug("Users folders for user with id '{}' werent founded", id);
        } else {
            log.debug("Users folders for user with id '{}' were founded counted '{}' pcs", id, userFolders.size());
        }
        return userFolders;
    }

    @Override
    public Folder findById(int id) {
        log.debug("Try to find folder with id '{}'", id);
        Folder folder = null;

        try {
            folder = jdbcTemplate.queryForObject(
                    FOLDER_GET_BY_ID,
                    new Object[]{id}, new FolderRowMapper()
            );
        } catch (DataAccessException e) {
            log.error("Query fails by finding folder with id '{}'", id);
            System.out.println(e.getMessage());
        }
        if (folder == null) {
            log.debug("Folder with id '{}' not founded", id);
        } else {
            log.debug("Folder with id '{}' founded", id);
        }
        return folder;
    }

    @Override
    public Folder findByName(String name) {
        log.debug("Try to find folder with name '{}'", name);
        Folder folder = null;

        try {
            folder = jdbcTemplate.queryForObject(
                    FOLDER_GET_BY_NAME,
                    new Object[]{name}, new FolderRowMapper()
            );
        } catch (DataAccessException e) {
            log.error("Query fails by finding folder with name '{}'", name);
            System.out.println(e.getMessage());
        }
        if (folder == null) {
            log.debug("Folder with name '{}' not founded", name);
        } else {
            log.debug("Folder with name '{}' founded", name);
        }
        return folder;
    }

    @Override
    public boolean moveEventsToGeneral(int id) {
        log.debug("Try to move events to general with id '{}'", id);
        int result = 0;
        try {
            result = jdbcTemplate.update(FOLDER_REMOVE_EVENTS, id);

        } catch (DataAccessException e) {
            log.error("Query fails by moving events to general with id '{}'", id);
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (result == 0) {
            log.debug("Moving events to general with id '{}' was failed", id);
            return false;
        } else {
            log.debug("Moving events to general with id '{}' was successful", id);
            return true;
        }
    }

    @Override
    public Folder findById(int id, int userId) {
        log.debug("Try to find folder with id '{}' for user with id '{}'", id, userId);
        Folder folder = null;

        try {
            folder = jdbcTemplate.queryForObject(
                    FOLDER_GET_BY_ID,
                    new Object[]{id, userId}, new FolderRowMapper()
            );
        } catch (DataAccessException e) {
            log.error("Query fails by finding folder with id '{}' for user with id '{}'", id, userId);
            System.out.println(e.getMessage());
        }
        if (folder == null) {
            log.debug("Folder with id '{}' for user with id '{}' wasnt founded", id, userId);
        } else {
            log.debug("Folder with id '{}' for user with id '{}' wasn founded", id, userId);
        }
        return folder;
    }

    @Override
    public Folder insert(Folder model) {
        log.debug("Try to insert folder with name '{}' by user with id '{}'", model.getName(), model.getUserId());
        int id;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("FOLDER")
                .usingGeneratedKeyColumns("FOLDER_ID");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", model.getName());
        parameters.put("user_id", model.getUserId());

        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setFolderId(id);
        } catch (DataAccessException e) {
            log.error("Query fails by inserting folder with name '{}' by user with id '{}'", model.getName(), model.getUserId());
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (model.getFolderId() != 0){
            log.debug("Inserting folder with name '{}' by user with id '{}' successful folder id '{}'", model.getName(), model.getUserId(), model.getFolderId());
        }
        return model;
    }

    @Override
    public Folder update(Folder model) {
        log.debug("Try to update folder with id '{}'", model.getFolderId());
        int result = 0;
        try {
            result = jdbcTemplate.update(FOLDER_UPDATE,
                    model.getName(), model.getFolderId());
        } catch (DataAccessException e) {
            log.error("Query fails by updating folder with id '{}'", model.getFolderId());
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (result == 0){
            log.debug("Updating folder with id '{}' not successful", model.getFolderId());
        }
        else {
            log.debug("Updating folder with id '{}' successful", model.getFolderId());
        }
        return model;
    }

    @Override
    public Folder delete(Folder model) {
        log.debug("Try to delete folder with id '{}'", model.getFolderId());
        int result = 0;
        try {
            result = jdbcTemplate.update(FOLDER_DELETE, model.getFolderId());
        } catch (DataAccessException e) {
            log.error("Query fails by deleting folder with id '{}'", model.getFolderId());
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }
        if (result == 0){
            log.debug("Deleting folder with id '{}' not successful", model.getFolderId());
        }
        else {
            log.debug("Deleting folder with id '{}' successful", model.getFolderId());
        }
        return model;
    }
}
