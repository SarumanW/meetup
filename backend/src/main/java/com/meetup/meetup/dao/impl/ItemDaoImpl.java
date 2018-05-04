package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.ItemDao;
import com.meetup.meetup.dao.rowMappers.ItemRowMapper;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
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

import static com.meetup.meetup.Keys.Key.*;

@Repository
@PropertySource("classpath:sqlDao.properties")
@PropertySource("classpath:strings.properties")
@PropertySource("classpath:image.properties")
public class ItemDaoImpl implements ItemDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment env;

    @Override
    public List<Item> findByUserId(int userId) {
        List<Item> items = new ArrayList<>();
        getUserItemsId(userId).forEach(itemId -> items.add(findById(itemId)));
        return items;
    }

    @Override
    public Item findById(int id) {
        Item item;
        try {
            item = jdbcTemplate.queryForObject(
                    env.getProperty(ITEM_FIND_BY_ID),
                    new Object[]{id}, new ItemRowMapper()
            );
        } catch (DataAccessException e) {
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return item;
    }

    @Override
    public Item insert(Item model) {
        int id;
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_ITEM)
                .usingGeneratedKeyColumns(ITEM_ITEM_ID);

        if (model.getImageFilepath() == null) {
            model.setImageFilepath(env.getProperty("image.default.filepath"));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ITEM_ITEM_ID, model.getItemId());
        parameters.put(ITEM_NAME, model.getName());
        parameters.put(ITEM_DESCRIPTION, model.getDescription());
        parameters.put(ITEM_USER_ID, model.getOwnerId());
        parameters.put(ITEM_IMAGE_FILEPATH, model.getImageFilepath());
        parameters.put(ITEM_LINK, model.getLink());
        parameters.put(ITEM_DUE_DATE, model.getDueDate() != null ? Date.valueOf(model.getDueDate()) : null);
        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setItemId(id);
        } catch (DataAccessException e) {

            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return model;
    }

    @Override
    public Item update(Item model) {
        try {
            jdbcTemplate.update(env.getProperty(ITEM_UPDATE),
                    model.getName(), model.getDescription(), model.getImageFilepath(), model.getLink(),
                    model.getDueDate(), model.getItemId());
        } catch (DataAccessException e) {
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return model;
    }

    // TODO: 04.05.2018 item deleting in all users, need userId in signature !!
    // TODO: 04.05.2018 delete item if it don't have references from users
    @Override
    public Item delete(Item model) {try {
        jdbcTemplate.update(env.getProperty(ITEM_DELETE), model.getItemId());
    } catch (DataAccessException e) {
        throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
    }
        return model;
    }

    @Override
    public List<Item> getPopularItems() {
        List<Item> items = new ArrayList<>();
        getPopularItemsId().forEach(itemId -> items.add(findById(itemId)));
        return items;
    }

    private List<Integer> getUserItemsId(int userId) {
        List<Integer> itemsIds;

        try {
            itemsIds = jdbcTemplate.queryForList(env.getProperty(ITEM_GET_ITEMS_ID_BY_USER_ID),
                    new Object[]{userId}, Integer.class);
        } catch (DataAccessException e) {
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return itemsIds;
    }

    private List<Integer> getPopularItemsId() {
        List<Integer> itemsIds;
        try {
            itemsIds = jdbcTemplate.queryForList(env.getProperty(ITEM_GET_POPULAR_ITEMS_ID), Integer.class);
        } catch (DataAccessException e) {
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return itemsIds;
    }
}
