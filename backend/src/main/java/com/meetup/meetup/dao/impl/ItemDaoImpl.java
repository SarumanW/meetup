package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.ItemDao;
import com.meetup.meetup.dao.rowMappers.ItemRowMapper;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.ItemPriority;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import static com.meetup.meetup.Keys.Key.*;

@Repository
@PropertySource("classpath:sqlDao.properties")
@PropertySource("classpath:strings.properties")
@PropertySource("classpath:image.properties")
public class ItemDaoImpl implements ItemDao {

    private static Logger log = LoggerFactory.getLogger(ItemDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment env;

    private final int numberOfPopularItem = 5;
    private final int numberOfSearchedItem = 5;

    @Override
    public List<Item> getWishListByUserId(int userId) {
        log.debug("Try get wish list by user id: '{}'", userId);

        List<Item> items = new ArrayList<>();
        getUserItemsId(userId).forEach((itemId) -> items.add(findByUserIdItemId(userId, itemId)));

        if (items.isEmpty()) {
            log.debug("Wish list don't found by user id: '{}'", userId);
        } else {
            log.debug("Wish list was found: '{}'", items);
        }
        return items;
    }

    @Override
    public Item findByUserIdItemId(int userId, int itemId) {
        log.debug("Try find item by user id: '{}' and item id: '{}'", userId, itemId);
        Item item = findById(itemId);
        try {
            jdbcTemplate.query(env.getProperty(ITEM_GET_PERSONAL_INFO_BY_ITEM_ID_USER_ID), new Object[]{userId, itemId},
                    (resultSet, i) -> {
                        Timestamp date = resultSet.getTimestamp(ITEM_DUE_DATE);
                        item.setDueDate(date == null ? null : date.toString());
                        item.setOwnerId(resultSet.getInt(USER_ITEM_USER_ID));
                        item.setBookerId(resultSet.getInt(USER_ITEM_BOOKER_ID));
                        item.setPriority(ItemPriority.values()[resultSet.getInt(USER_ITEM_PRIORITY_ID) - 1]);
                        item.setLike(resultSet.getInt(LLIKE_LIKE_ID) != 0);
                        return item;
                    });
        } catch (DataAccessException e) {
            log.error("Query fails by find item by user id: '{}' and item id: '{}'", userId, itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return item;
    }

    @Override
    public Item findById(int itemId) {
        log.debug("Try find item by item id: '{}'", itemId);
        Item item;
        try {
            item = jdbcTemplate.queryForObject(env.getProperty(ITEM_FIND_BY_ID), new Object[]{itemId}, new ItemRowMapper());
            item.setTags(getTagsByItemId(itemId));
        } catch (DataAccessException e) {
            log.error("Query fails by find item by item id: '{}'", itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return item;
    }

    @Override
    @Transactional
    public Item insert(Item model) {
        log.debug("Try insert item '{}'", model);
        SimpleJdbcInsert insertItem = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_ITEM)
                .usingGeneratedKeyColumns("ITEM_ID");
        if (model.getImageFilepath() == null) {
            model.setImageFilepath(env.getProperty("image.default.filepath"));
        }
        Map<String, Object> itemParameters = new HashMap<>();
        itemParameters.put(ITEM_ITEM_ID, model.getItemId());
        itemParameters.put(ITEM_NAME, model.getName());
        itemParameters.put(ITEM_DESCRIPTION, model.getDescription());
        itemParameters.put(ITEM_IMAGE_FILEPATH, model.getImageFilepath());
        itemParameters.put(ITEM_LINK, model.getLink());
        try {
            model.setItemId(insertItem.executeAndReturnKey(itemParameters).intValue());
            addTags(model.getTags(), model.getItemId());
            addToUserWishList(model.getOwnerId(), model.getItemId(), model.getDueDate(), model.getPriority());
        } catch (DataAccessException e) {
            log.error("Query fails by insert item '{}'", model);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (model.getItemId() != 0) {
            log.debug("Item was added with id '{}'", model.getItemId());
        } else {
            log.debug("Item wasn't added item '{}'", model);
        }
        return model;
    }

    @Override
    public List<Item> getPopularItems() {
        log.debug("Try get popular items");

        List<Item> items = new ArrayList<>();
        getPopularItemsId().forEach(itemId -> items.add(findById(itemId)));

        if (items.isEmpty()) {
            log.debug("Popular items don't found");
        } else {
            log.debug("Popular items was found: '{}'", items);
        }
        return items;
    }

    @Override
    public Item addToUserWishList(int userId, int itemId, String dueDate, ItemPriority priority) {
        log.debug("Try add to wish list by user id: '{}', item id: '{}', priority: '{}'", userId, itemId, priority);
        try {
            int result = jdbcTemplate.update(env.getProperty(ITEM_UPDATE_USER_ITEM),
                    userId, itemId, dueDate, priority.ordinal() + 1);

            if (result != 0) {
                log.debug("Item by user id: '{}', item id: '{}', priority: '{}' was added to wish list",
                        userId, itemId, priority);
            } else {
                log.debug("Item by user id: '{}', item id: '{}', priority: '{}' was not added to wish list",
                        userId, itemId, priority);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by add item to wish list by user id: '{}', item id: '{}', priority: '{}'",
                    userId, itemId, priority);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findById(itemId);
    }

    @Override
    @Transactional
    public Item deleteFromUserWishList(int userId, int itemId) {
        Item deletedItem = findByUserIdItemId(userId, itemId);
        log.debug("Try to remove from wish list by user id: '{}', item id: '{}'", userId, itemId);
        try {
            int result = jdbcTemplate.update(env.getProperty(ITEM_DELETE_FROM_WISH_LIST), userId, itemId);

            if (result != 0) {
                log.debug("Item by user id: '{}', item id: '{}' was removed from wish list", userId, itemId);
            } else {
                log.debug("Item by user id: '{}', item id: '{}' was not removed from wish list", userId, itemId);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by remove item from wish list by user id: '{}', item id: '{}'", userId, itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return delete(deletedItem);
    }

    @Override
    public Item addBookerForItem(int ownerId, int itemId, int bookerId) {
        log.debug("Try to add booker by owner id: '{}', item id: '{}'", ownerId, itemId);
        try {
            int result = jdbcTemplate.update(env.getProperty(ITEM_SET_BOOKER_ID_FOR_ITEM),
                    bookerId, ownerId, itemId);

            if (result != 0) {
                log.debug("Booker by owner id: '{}', item id: '{}' was added", ownerId, itemId);
            } else {
                log.debug("Booker by owner id: '{}', item id: '{}' was not added", ownerId, itemId);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by remove booker by owner id: '{}', item id: '{}'", ownerId, itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findById(itemId);
    }

    @Override
    public Item removeBookerForItem(int ownerId, int itemId) {
        log.debug("Try to remove booker by owner id: '{}', item id: '{}'", ownerId, itemId);
        try {
            int result = jdbcTemplate.update(env.getProperty(ITEM_SET_BOOKER_ID_FOR_ITEM),
                    null, ownerId, itemId);

            if (result != 0) {
                log.debug("Booker by owner id: '{}', item id: '{}' was removed", ownerId, itemId);
            } else {
                log.debug("Booker by owner id: '{}', item id: '{}' was not removed", ownerId, itemId);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by remove booker by owner id: '{}', item id: '{}'", ownerId, itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findById(itemId);
    }

    @Override
    public Item addLike(int itemId, int userId) {
        log.debug("Try to add like by item id: '{}', user id: '{}'", itemId, userId);
        try {
            int result = jdbcTemplate.update(env.getProperty(ITEM_ADD_LIKE_BY_ITEM_ID_USER_ID),
                    itemId, userId);

            if (result != 0) {
                log.debug("Like by item id: '{}', user id: '{}' was added", itemId, userId);
            } else {
                log.debug("Like by item id: '{}', user id: '{}' was not added", itemId, userId);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by add like by item id: '{}', user id: '{}'", itemId, userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findById(itemId);
    }

    @Override
    public Item removeLike(int itemId, int userId) {
        log.debug("Try to remove like by item id: '{}', user id: '{}'", itemId, userId);
        try {
            int result = jdbcTemplate.update(env.getProperty(ITEM_REMOVE_LIKE_BY_ITEM_ID_USER_ID),
                    itemId, userId);

            if (result != 0) {
                log.debug("Like by item id: '{}', user id: '{}' was removed", itemId, userId);
            } else {
                log.debug("Like by item id: '{}', user id: '{}' was not removed", itemId, userId);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by remove like by item id: '{}', user id: '{}'", itemId, userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findById(itemId);
    }

    @Override
    public List<Item> findBookedItemsByUserId(int userId) {
        log.debug("Try get booked items list by user id: '{}'", userId);
        List<Item> items;
        try {
            items = jdbcTemplate.query(env.getProperty(ITEM_GET_BOOKED_ITEMS_BY_USER_ID), new Object[]{userId},
                    (resultSet, i) -> {
                        Item item = new Item();
                        item.setItemId(resultSet.getInt(ITEM_ITEM_ID));
                        item.setName(resultSet.getString(ITEM_NAME));
                        item.setDescription(resultSet.getString(ITEM_DESCRIPTION));
                        item.setImageFilepath(resultSet.getString(ITEM_IMAGE_FILEPATH));
                        item.setLink(resultSet.getString(ITEM_LINK));
                        Timestamp date = resultSet.getTimestamp(ITEM_DUE_DATE);
                        item.setDueDate(date == null ? null : date.toString());
                        item.setOwnerId(resultSet.getInt(USER_ITEM_USER_ID));
                        item.setBookerId(resultSet.getInt(USER_ITEM_BOOKER_ID));
                        item.setPriority(ItemPriority.values()[resultSet.getInt(USER_ITEM_PRIORITY_ID) - 1]);
                        item.setLikes(resultSet.getInt(ITEM_LIKES));
                        item.setLike(resultSet.getInt(LLIKE_LIKE_ID) != 0);
                        item.setTags(getTagsByItemId(item.getItemId()));
                        return item;
                    });
        } catch (DataAccessException e) {
            log.error("Query fails by find item by user id: '{}' and item id: '{}'", userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (items.isEmpty()) {
            log.debug("Booked items list don't found by user id: '{}'", userId);
        } else {
            log.debug("Booked items list was found: '{}'", items);
        }
        return items;
    }

    @Override
    public List<Item> findItemsByTagName(String[] tagNames) {
        log.debug("Try get items list by tag name: '{}'", Arrays.toString(tagNames));

        List<Item> items = new ArrayList<>();
        getItemsIdByTagName(tagNames).forEach((itemId) -> items.add(findById(itemId)));

        if (items.isEmpty()) {
            log.debug("Items list don't found by tag name: '{}'", Arrays.toString(tagNames));
        } else {
            log.debug("Items list was found: '{}'", items);
        }
        return items;
    }

    @Override
    @Transactional
    public Item update(Item model) {
        if (getItemNumberOfUsers(model.getItemId()) != 1) {
            log.debug("Try insert altered item '{}'", model);
            deleteFromUserWishList(model.getOwnerId(), model.getItemId());
            insert(model);
        } else {
            log.debug("Try change existing item");
            try {
                int result = jdbcTemplate.update(env.getProperty(ITEM_UPDATE),
                        model.getName(), model.getDescription(), model.getImageFilepath(), model.getLink(), model.getItemId());
                result += jdbcTemplate.update(env.getProperty(ITEM_UPDATE_USER_ITEM_INFO),
                        model.getPriority().ordinal() + 1, model.getDueDate(), model.getOwnerId(), model.getItemId());
                if (result > 0) {
                    log.debug("Item was updated item: '{}'", model);
                } else {
                    log.debug("Item was not updated item");
                }
                addTags(model.getTags(), model.getItemId());
            } catch (DataAccessException e) {
                log.error("Query fails by updating item: '{}'", model);
                throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
            }
        }
        return model;
    }

    @Override
    @Transactional
    public Item delete(Item model) {
        if (getItemNumberOfUsers(model.getItemId()) == 0) {
            deleteTagsByItemId(model.getItemId());
            log.debug("Try to delete item by item id: '{}'", model.getItemId());
            try {
                jdbcTemplate.update(env.getProperty(ITEM_DELETE), model.getItemId());
            } catch (DataAccessException e) {
                log.error("Query fails by deleting item with item id: '{}'", model.getItemId());
                throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
            }
        }
        return model;
    }

    @Override
    public List<String> searchTag(String aboutTag) {
        log.debug("Try to search tags name by about tag: '{}'", aboutTag);
        try {
            return jdbcTemplate.queryForList(
                    env.getProperty(TAG_SEARCH_TAGS_NAME), String.class, "%" + aboutTag + "%", numberOfSearchedItem);
        } catch (DataAccessException e) {
            log.error("Query fails by searching tags name by about tag: '{}'", aboutTag);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }

    // TODO: 09.05.2018 don't work
    private List<Integer> getItemsIdByTagName(String[] tagNames) {
        log.debug("Try to get items id by tag name: '{}'", Arrays.toString(tagNames));
        List<Integer> itemsIds;
        try {
            itemsIds = jdbcTemplate.queryForList(env.getProperty(ITEM_GET_ITEMS_ID_BY_TAG_NAMES), Integer.class,
                    tagNames, tagNames.length);
        } catch (DataAccessException e) {
            log.error("Query fails by finding item's ids with tag name: '{}'", Arrays.toString(tagNames));
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (itemsIds.isEmpty()) {
            log.debug("Item's ids not found by tag name: '{}'", Arrays.toString(tagNames));
        } else {
            log.debug("Item's ids were wound by tag naem: '{}'", Arrays.toString(tagNames));
        }
        return itemsIds;
    }

    private List<Integer> getUserItemsId(int userId) {
        log.debug("Try to getUserItemsIds by user id '{}'", userId);

        List<Integer> itemsIds;
        try {
            itemsIds = jdbcTemplate.queryForList(env.getProperty(ITEM_GET_ITEMS_ID_BY_USER_ID),
                    new Object[]{userId}, Integer.class);
        } catch (DataAccessException e) {
            log.error("Query fails by finding user item's ids with user id '{}'", userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (itemsIds.isEmpty()) {
            log.debug("User item's ids not found by user id '{}'", userId);
        } else {
            log.debug("User item's ids were wound by user id '{}'", userId);
        }
        return itemsIds;
    }

    private List<Integer> getPopularItemsId() {
        log.debug("Try to get popular item's id");

        List<Integer> itemsIds;
        try {
            itemsIds = jdbcTemplate.queryForList(
                    env.getProperty(ITEM_GET_POPULAR_ITEMS_ID), new Object[]{numberOfPopularItem}, Integer.class);
        } catch (DataAccessException e) {
            log.error("Query fails by finding popular item's ids");
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        if (itemsIds.isEmpty()) {
            log.debug("Popular item's ids not found by user id ");
        } else {
            log.debug("Popular item's ids were wound by user id ");
        }
        return itemsIds;
    }

    private void addTags(List<String> tags, int itemId) {
        deleteTagsByItemId(itemId);
        log.debug("Try to find tags by item id: '{}'", itemId);
        Map<Integer, String> tagsId = new HashMap<>();
        tags.forEach((tag) -> {
            try {
                int id = jdbcTemplate.queryForObject(env.getProperty(ITEM_GET_TAG_ID), new Object[]{tag}, Integer.class);
                if (id == 0) {
                    log.debug("Try to add new tag: '{}'", tag);
                    SimpleJdbcInsert insertItem = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                            .withTableName(TABLE_TAG)
                            .usingGeneratedKeyColumns(TAG_TAG_ID);
                    Map<String, Object> itemParameters = new HashMap<>();
                    itemParameters.put(TAG_TAG_NAME, tag);
                    try {
                        id = insertItem.executeAndReturnKey(itemParameters).intValue();
                        tagsId.put(id, tag);
                    } catch (DataAccessException e) {
                        log.error("Query fails by add new tag by tag name: '{}', tag id: '{}'", tag, id);
                        throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
                    }
                } else {
                    log.debug("Tag: '{}' exist, tag id: '{}'", tag, id);
                    tagsId.put(id, tag);
                }
            } catch (DataAccessException e) {
                log.error("Query fails by find tag id by tag name: '{}'", tag);
                throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
            }
        });
        log.debug("Try add tag to item: '{}'", itemId);
        tagsId.forEach((tagId, tagName) -> {
            try {
                jdbcTemplate.update(env.getProperty(ITEM_ADD_TAG_TO_ITEM), itemId, tagId);
            } catch (DataAccessException e) {
                log.error("Query fails by add tag to item by tag id: '{}', item id: '{}'", tagId, itemId);
                throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
            }
        });
    }

    private void deleteTagsByItemId(int itemId) {
        log.debug("Try to delete tags by item id: '{}'", itemId);
        try {
            jdbcTemplate.update(env.getProperty(ITEM_DELETE_TAGS), itemId);
        } catch (DataAccessException e) {
            log.error("Query fails by delete item's tags by item id: '{}'", itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }

    private int getItemNumberOfUsers(int itemId) {
        log.debug("Try to find item number of users by item id: '{}'", itemId);
        try {
            return jdbcTemplate.queryForObject(
                    env.getProperty(ITEM_GET_NUMBER_OF_ITEM_USERS), new Object[]{itemId}, Integer.class);
        } catch (DataAccessException e) {
            log.error("Query fails by find item number of users by item id: '{}'", itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }

    private List<String> getTagsByItemId(int itemId) {
        log.debug("Try to find item tags by item id: '{}'", itemId);
        try {
            return jdbcTemplate.queryForList(env.getProperty(ITEM_GET_TAG_BY_ITEM_ID), new Object[]{itemId}, String.class);
        } catch (DataAccessException e) {
            log.error("Query fails by find item tags by item id: '{}'", itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }
}
