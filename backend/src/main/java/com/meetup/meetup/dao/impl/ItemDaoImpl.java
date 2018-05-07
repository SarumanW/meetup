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
import java.util.*;

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

    private final int numberOfPopularItem = 5;

    @Override
    public List<Item> getWishListByUserId(int userId) {
        List<Item> items = new ArrayList<>();
        getUserItemsId(userId).forEach((itemId) -> items.add(findByUserIdItemId(userId, itemId)));
        return items;
    }

    @Override
    public Item findByUserIdItemId(int userId, int itemId) {
        Item item = findById(itemId);
        item.setPriority(getItemPriorityByUserIdItemId(userId, itemId));
        item.setOwnerId(userId);
        item.setBookerId(jdbcTemplate.queryForObject(
                env.getProperty(ITEM_GET_BOOKER_ID_BY_ITEM_ID_USER_ID), new Object[]{itemId, userId}, Integer.class));
        return item;
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

    // TODO: 05.05.2018 date, transactions
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
            addTags(model.getTags(), model.getItemId());
            addToUserWishList(model.getOwnerId(), model.getItemId(), model.getPriority());
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

    @Override
    public Item addToUserWishList(int userId, int itemId, ItemPriority priority) {
        jdbcTemplate.update(env.getProperty(ITEM_UPDATE_USER_ITEM),
                userId, itemId, priority.ordinal() + 1);
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
    
    @Override
    public Item update(Item model) {
        int numberOfUsers = jdbcTemplate.queryForObject(env.getProperty(ITEM_GET_NUMBER_OF_ITEM_USERS), new Object[]{model.getItemId()}, Integer.class);
        if (numberOfUsers > 1) {
            deleteFromUserWishList(model.getOwnerId(), model.getItemId());
            insert(model);
        } else {
            try {
                jdbcTemplate.update(env.getProperty(ITEM_UPDATE),
                        model.getName(), model.getDescription(), model.getImageFilepath(), model.getLink(),
                        model.getDueDate(), model.getItemId());
                addTags(model.getTags(), model.getItemId());
            } catch (DataAccessException e) {
                throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
            }
        }
        return model;
    }

    // item deleting in all users
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
            itemsIds = jdbcTemplate.queryForList(
                    env.getProperty(ITEM_GET_POPULAR_ITEMS_ID), new Object[]{numberOfPopularItem}, Integer.class);
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

    private void addTags(List<String> tags, int itemId) {
        jdbcTemplate.update("DELETE FROM TAG_ITEM WHERE ITEM_ID = ?", itemId);

        Map<Integer, String> tagsId = new HashMap<>();
        tags.forEach((tag) -> {
            try {
                int id = jdbcTemplate.queryForObject(env.getProperty(ITEM_GET_TAG_ID), new Object[]{tag}, Integer.class);
            if (id == 0) {
                SimpleJdbcInsert insertItem = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                        .withTableName(env.getProperty(TABLE_TAG))
                        .usingGeneratedKeyColumns(env.getProperty(TAG_TAG_ID));
                Map<String, Object> itemParameters = new HashMap<>();
                itemParameters.put(env.getProperty(TAG_TAG_NAME), tag);
                try {
                    id = insertItem.executeAndReturnKey(itemParameters).intValue();
                    tagsId.put(id, tag);
                } catch (DataAccessException e) {
                    throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
                }
            } else {
                tagsId.put(id, tag);
            }
            } catch (DataAccessException e) {
                throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
            }
        });
        tagsId.forEach((tagId, tagName)->{
            jdbcTemplate.update(env.getProperty(ITEM_ADD_TAG_TO_ITEM), itemId, tagId);
        });
    }
}
