package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.FolderDao;
import com.meetup.meetup.dao.rowMappers.EventRowMapper;
import com.meetup.meetup.dao.rowMappers.FolderRowMapper;
import com.meetup.meetup.entity.*;
import com.meetup.meetup.exception.DatabaseWorkException;
import oracle.jdbc.OracleTypes;
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

    @Autowired
    private Environment env;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Folder> getUserFolders(int id) {
        List<Folder> userFolders = new ArrayList<>();

        try{
            userFolders = jdbcTemplate.query(GET_USER_FOLDERS,
                    new Object[]{id}, new FolderRowMapper());
        } catch (DataAccessException e){
            System.out.println(e.getMessage());
        }

        return userFolders;
    }

    @Override
    public Folder findById(int id) {
        Folder folder = null;

        try{
            folder = jdbcTemplate.queryForObject(
                    FOLDER_GET_BY_ID,
                    new Object[]{id}, new FolderRowMapper()
            );
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return folder;
    }

    @Override
    public Folder findByName(String name) {
        Folder folder = null;

        try{
            folder = jdbcTemplate.queryForObject(
                    FOLDER_GET_BY_NAME,
                    new Object[]{name}, new FolderRowMapper()
            );
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return folder;
    }

    @Override
    public boolean moveEventsToGeneral(int id) {

        try{
            jdbcTemplate.update(FOLDER_REMOVE_EVENTS, id);
            return true;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }

    }

    @Override
    public Folder findById(int id, int userId) {
        Folder folder = null;

        try{
            folder = jdbcTemplate.queryForObject(
                    FOLDER_GET_BY_ID,
                    new Object[]{id, userId}, new FolderRowMapper()
            );
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return folder;
    }

    @Override
    public Folder insert(Folder model) {

        int id;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("FOLDER")
                .usingGeneratedKeyColumns("FOLDER_ID");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", model.getName());
        parameters.put("user_id", model.getUserId());

        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setFolderId(id);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }

        return model;
    }

    @Override
    public Folder update(Folder model) {
        try{
            jdbcTemplate.update(FOLDER_UPDATE,
                    model.getName(), model.getFolderId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }

        return model;
    }

    @Override
    public Folder delete(Folder model) {
        try {
            jdbcTemplate.update(FOLDER_DELETE, model.getFolderId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }

        return model;
    }
}
