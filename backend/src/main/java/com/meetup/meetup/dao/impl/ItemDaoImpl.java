package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.ItemDao;
import com.meetup.meetup.dao.rowMappers.ItemRowMapper;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.ItemPriority;
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

    // TODO: 05.05.2018 performance optimization
    @Override
    public List<Item> findByUserId(int userId) {
        List<Item> items = new ArrayList<>();
        getUserItemsId(userId).forEach((itemId) -> {
            Item item = findById(itemId);
            item.setPriority(getItemPriorityByUserIdItemId(userId, itemId));
            item.setOwnerId(userId);
            item.setBookerId(jdbcTemplate.queryForObject(
                    env.getProperty(ITEM_GET_BOOKER_ID_BY_ITEM_ID_USER_ID), new Object[]{itemId, userId}, Integer.class));
            items.add(item);
        });
        return items;
    }

    @Override
    public Item findById(int itemId) {
        Item item;
        try {
            item = jdbcTemplate.queryForObject(env.getProperty(ITEM_FIND_BY_ID), new Object[]{itemId}, new ItemRowMapper());
            item.setTags(jdbcTemplate.queryForList(env.getProperty(ITEM_GET_TAG_BY_ITEM_ID), new Object[]{itemId}, String.class));
        } catch (DataAccessException e) {
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return item;
    }

    // TODO: 05.05.2018 insert tags
    @Override
    public Item insert(Item model) {
        int id;
        SimpleJdbcInsert insertItem = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_ITEM)
                .usingGeneratedKeyColumns(ITEM_ITEM_ID);
        if (model.getImageFilepath() == null) {
            model.setImageFilepath(env.getProperty("image.default.filepath"));
        }
        Map<String, Object> itemParameters = new HashMap<>();
        itemParameters.put(ITEM_ITEM_ID, model.getItemId());
        itemParameters.put(ITEM_NAME, model.getName());
        itemParameters.put(ITEM_DESCRIPTION, model.getDescription());
        itemParameters.put(ITEM_IMAGE_FILEPATH, model.getImageFilepath());
        itemParameters.put(ITEM_LINK, model.getLink());
        itemParameters.put(ITEM_DUE_DATE, model.getDueDate() != null ? Date.valueOf(model.getDueDate()) : null);
        try {
            id = insertItem.executeAndReturnKey(itemParameters).intValue();
            model.setItemId(id);
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

    // TODO: 05.05.2018 priority
    @Override
    public Item addToUserWishList(int userId, int itemId, ItemPriority priority) {
        jdbcTemplate.update(env.getProperty(ITEM_UPDATE_USER_ITEM),
                userId, userId, priority.ordinal() + 1);
        return findById(itemId);
    }

    @Override
    public Item deleteFromUserWishList(int userId, int itemId) {
        Item deletedItem = findById(itemId);
        jdbcTemplate.update(env.getProperty(ITEM_DELETE_FROM_WISH_LIST), userId, itemId);
        return deletedItem;
    }

    @Override
    public Item addBookerForItem(int ownerId, int itemId, int bookerId) {
        jdbcTemplate.update(env.getProperty(ITEM_SET_BOOKER_ID_FOR_ITEM),
                bookerId, ownerId, itemId);
        return findById(itemId);
    }

    @Override
    public Item removeBookerForItem(int ownerId, int itemId) {
        jdbcTemplate.update(env.getProperty(ITEM_SET_BOOKER_ID_FOR_ITEM),
                null, ownerId, itemId);
        return findById(itemId);
    }

    // TODO: 05.05.2018 if any user change item it change in all users who have it item
    @Override
    public Item update(Item model) {
//        try {
//            jdbcTemplate.update(env.getProperty(ITEM_UPDATE),
//                    model.getName(), model.getDescription(), model.getImageFilepath(), model.getLink(),
//                    model.getDueDate(), model.getItemId());
//        } catch (DataAccessException e) {
//            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
//        }
        return model;
    }

    // TODO: 04.05.2018 item deleting in all users, need userId in signature !!
    // TODO: 04.05.2018 delete item if it don't have references from users
    @Override
    public Item delete(Item model) {
//        try {
//            jdbcTemplate.update(env.getProperty(ITEM_DELETE), model.getItemId());
//        } catch (DataAccessException e) {
//            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
//        }
        return model;
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

    private ItemPriority getItemPriorityByUserIdItemId(int userId, int itemId) {
        ItemPriority priority;
        try {
            priority = ItemPriority.valueOf(jdbcTemplate.queryForObject(env.getProperty(ITEM_GET_PRIORITY_BY_USER_ID),
                    new Object[]{userId, itemId}, String.class));
        } catch (DataAccessException e) {
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return priority;
    }
}
