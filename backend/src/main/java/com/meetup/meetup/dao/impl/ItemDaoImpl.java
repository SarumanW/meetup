package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.AbstractDao;
import com.meetup.meetup.dao.ItemDao;
import com.meetup.meetup.dao.rowMappers.ItemFullInfoRowMapper;
import com.meetup.meetup.dao.rowMappers.ItemRowMapper;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.ItemPriority;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
import com.meetup.meetup.exception.runtime.frontend.detailed.ItemIsInWishListException;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

import static com.meetup.meetup.keys.Key.*;

@Repository
@PropertySource("classpath:sqlDao.properties")
@PropertySource("classpath:strings.properties")
@PropertySource("classpath:image.properties")
public class ItemDaoImpl extends AbstractDao<Item> implements ItemDao {

    public ItemDaoImpl() {
        log = LoggerFactory.getLogger(ItemDaoImpl.class);
    }

    private final static int NUMBER_OF_POPULAR_ITEMS = 5;
    private final static int NUMBER_OF_SEARCHED_ITEMS = 5;

    @Override
    public List<Item> getWishListByUserId(int userId) {
        log.debug("Try get wish list by user id: '{}'", userId);

        List<Item> items = new ArrayList<>();
        try {
            items = jdbcTemplate.query(
                    env.getProperty(ITEM_GET_ITEMS_BY_USER_ID), new Object[]{userId}, new ItemFullInfoRowMapper());
            items.forEach(item -> item.setTags(getTagsByItemId(item.getItemId())));
        } catch (DataAccessException e) {
            log.error("Query fails by finding user items with user id '{}'", userId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

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
            item.setLike(getLikeId(userId, itemId) != 0);
            jdbcTemplate.query(env.getProperty(ITEM_GET_PERSONAL_INFO_BY_ITEM_ID_USER_ID), new Object[]{userId, itemId},
                    (resultSet, i) -> {
                        Timestamp date = resultSet.getTimestamp(ITEM_DUE_DATE);
                        item.setDueDate(date == null ? null : date.toString());
                        item.setOwnerId(resultSet.getInt(USER_ITEM_USER_ID));
                        item.setBookerId(resultSet.getInt(USER_ITEM_BOOKER_ID));
                        item.setPriority(ItemPriority.values()[resultSet.getInt(USER_ITEM_PRIORITY_ID) - 1]);
                        return item;
                    });
        } catch (DataAccessException e) {
            log.error("Query fails by find item by user id: '{}' and item id: '{}'", userId, itemId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        log.debug("Item for user with id '{}' and item with id '{}' is '{}'", userId, itemId, item);
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
            log.error("Query fails by find item by item id: '{}'", itemId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        log.debug("Item with id '{}' is '{}'", itemId, item);
        return item;
    }

    @Override
    @Transactional
    public Item insert(Item model) {
        log.debug("Try insert item '{}'", model);

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
        try {
            model.setItemId(insertItem.executeAndReturnKey(itemParameters).intValue());
            addTags(model.getTags(), model.getItemId());
            addToUserWishList(model);
        } catch (DataAccessException e) {
            log.error("Query fails by insert item '{}'", model, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        log.debug("Item was added with id '{}'", model.getItemId());
        return model;
    }

    @Override
    public List<Item> getPopularItems() {
        log.debug("Try get popular items");

        List<Item> items;
        try {
            items = jdbcTemplate.query(
                    env.getProperty(ITEM_GET_POPULAR_ITEMS), new Object[]{NUMBER_OF_POPULAR_ITEMS}, new ItemRowMapper());
            items.forEach(item -> item.setTags(getTagsByItemId(item.getItemId())));
        } catch (DataAccessException e) {
            log.error("Query fails by finding popular items", e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        log.debug("Popular items are '{}'", items);
        return items;
    }

    @Override
    public Item addToUserWishList(Item item) {
        log.debug("Try add to wish list by user id: '{}', item id: '{}', priority: '{}'",
                item.getOwnerId(), item.getItemId(), item.getPriority());
        System.out.println(item);
        try {
            int result = jdbcTemplate.update(env.getProperty(ITEM_UPDATE_USER_ITEM),
                    item.getOwnerId(), item.getItemId(), item.getDueDate(), item.getPriority().ordinal() + 1);

            if (result != 0) {
                log.debug("Item by user id: '{}', item id: '{}', due date: '{}', priority: '{}' was added to wish list",
                        item.getOwnerId(), item.getItemId(), item.getDueDate(), item.getPriority());
            } else {
                log.debug("Item by user id: '{}', item id: '{}', due date: '{}', priority: '{}' was not added to wish list",
                        item.getOwnerId(), item.getItemId(), item.getDueDate(), item.getPriority());
            }
        } catch (DuplicateKeyException e) {
            log.error("Duplicate key exception for adding to user wish list for item '{}'", item, e);
            throw new ItemIsInWishListException(env.getProperty(EXCEPTION_ITEM_IS_IN_WISHLIST));
        } catch (DataAccessException e) {
            e.printStackTrace();
            log.error("Query fails by add item to wish list by user id: '{}', item id: '{}', priority: '{}'",
                    item.getOwnerId(), item.getItemId(), item.getPriority(), e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return item;
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
            log.error("Query fails by remove item from wish list by user id: '{}', item id: '{}'", userId, itemId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return delete(deletedItem);
    }

    @Override
    public Item addBookerForItem(int ownerId, int itemId, int bookerId) {
        log.debug("Try to add booker by owner id: '{}', item id: '{}'", ownerId, itemId);
        try {
            int result = jdbcTemplate.update(env.getProperty(ITEM_ADD_BOOKER_ID_FOR_ITEM),
                    bookerId, ownerId, itemId);

            if (result != 0) {
                log.debug("Booker by owner id: '{}', item id: '{}' was added", ownerId, itemId);
            } else {
                log.debug("Booker by owner id: '{}', item id: '{}' was not added", ownerId, itemId);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by remove booker by owner id: '{}', item id: '{}'", ownerId, itemId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findByUserIdItemId(ownerId, itemId);
    }

    @Override
    public Item removeBookerForItem(int ownerId, int itemId, int bookerId) {
        log.debug("Try to remove booker by owner id: '{}', item id: '{}', booker id: '{}'", ownerId, itemId, bookerId);
        try {
            int result = jdbcTemplate.update(env.getProperty(ITEM_DELETE_BOOKER_ID_FOR_ITEM),
                    null, ownerId, itemId, bookerId);

            if (result != 0) {
                log.debug("Booker by owner id: '{}', item id: '{}', booker id: '{}' was removed", ownerId, itemId, bookerId);
            } else {
                log.debug("Booker by owner id: '{}', item id: '{}', booker id: '{}' was not removed", ownerId, itemId, bookerId);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by remove booker by owner id: '{}', item id: '{}', booker id: '{}'", ownerId, itemId, bookerId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findByUserIdItemId(ownerId, itemId);
    }

    @Override
    public Item addLike(int itemId, int userId) {
        log.debug("Try to add like by item id: '{}', user id: '{}'", itemId, userId);
        try {
            SimpleJdbcInsert insertItem = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                    .withTableName(TABLE_LLIKE)
                    .usingGeneratedKeyColumns(LLIKE_LIKE_ID);
            Map<String, Object> itemParameters = new HashMap<>();
            itemParameters.put(LLIKE_ITEM_ID, itemId);
            itemParameters.put(LLIKE_USER_ID, userId);

            insertItem.execute(itemParameters);
            log.debug("Like by item id: '{}', user id: '{}' was added", itemId, userId);

        } catch (DataAccessException e) {
            log.error("Query fails by add like by item id: '{}', user id: '{}'", itemId, userId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findByUserIdItemId(userId, itemId);
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
            log.error("Query fails by remove like by item id: '{}', user id: '{}'", itemId, userId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findByUserIdItemId(userId, itemId);
    }

    @Override
    public List<Item> findBookedItemsByUserId(int userId) {
        log.debug("Try get booked items list by user id: '{}'", userId);
        List<Item> items;
        try {
            items = jdbcTemplate.query(env.getProperty(ITEM_GET_BOOKED_ITEMS_BY_USER_ID), new Object[]{userId},
                    new ItemFullInfoRowMapper());
            items.forEach(item -> {
                item.setTags(getTagsByItemId(item.getItemId()));
            });
        } catch (DataAccessException e) {
            log.error("Query fails by find item by user id: '{}' and item id: '{}'", userId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        log.debug("Booked items for user with id '{}' are '{}'", userId, items);
        return items;
    }

    @Override
    public List<Item> findItemsByTagName(String[] tagNames) {
        log.debug("Try get items list by tag name: '{}'", Arrays.toString(tagNames));

        StringBuilder query = new StringBuilder(env.getProperty(ITEM_GET_ITEMS_BY_TAG_NAMES));

        for (int i = 1; i < tagNames.length; i++) {
            query.insert(query.indexOf("?"), "?,");
        }

        List<Object> params = new ArrayList<>(Arrays.asList(tagNames));
        params.add(tagNames.length);
        params.add(NUMBER_OF_SEARCHED_ITEMS);

        List<Item> items;
        try {
            items = jdbcTemplate.query(query.toString(), params.toArray(), new ItemRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by finding item's ids with tag name: '{}'", Arrays.toString(tagNames), e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        log.debug("Items with tag names '{}' are '{}'", tagNames, items);
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
                log.error("Query fails by updating item: '{}'", model, e);
                throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
            }
        }
        return model;
    }

    @Override
    @Transactional
    public Item delete(Item model) {
        if (getItemNumberOfUsers(model.getItemId()) == 0) {
            log.debug("Try to delete item by item id: '{}'", model.getItemId());
            try {
                int result = jdbcTemplate.update(env.getProperty(ITEM_DELETE), model.getItemId());
                if (result == 0) {
                    log.error("Not deleted item with item id: '{}'", model.getItemId());
                } else {
                    log.error("Successfully deleted item with item id: '{}'", model.getItemId());
                }
            } catch (DataAccessException e) {
                log.error("Query fails by deleting item with item id: '{}'", model.getItemId(), e);
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
                    env.getProperty(TAG_SEARCH_TAGS_NAME), String.class, "%" + aboutTag + "%", NUMBER_OF_SEARCHED_ITEMS);
        } catch (DataAccessException e) {
            log.error("Query fails by searching tags name by about tag: '{}'", aboutTag, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }

    @Override
    public boolean itemExistInWishList(Item item) {
        log.debug("Check whether an item exists '{}'", item);
        return getWishListByUserId(item.getOwnerId()).contains(item);
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
                        log.error("Query fails by add new tag by tag name: '{}', tag id: '{}'", tag, id, e);
                        throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
                    }
                } else {
                    log.debug("Tag: '{}' exist, tag id: '{}'", tag, id);
                    tagsId.put(id, tag);
                }
            } catch (DataAccessException e) {
                log.error("Query fails by find tag id by tag name: '{}'", tag, e);
                throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
            }
        });
        log.debug("Try add tag to item: '{}'", itemId);
        tagsId.forEach((tagId, tagName) -> {
            try {
                jdbcTemplate.update(env.getProperty(ITEM_ADD_TAG_TO_ITEM), itemId, tagId);
            } catch (DataAccessException e) {
                log.error("Query fails by add tag to item by tag id: '{}', item id: '{}'", tagId, itemId, e);
                throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
            }
        });
    }

    private void deleteTagsByItemId(int itemId) {
        log.debug("Try to delete tags by item id: '{}'", itemId);
        try {
            jdbcTemplate.update(env.getProperty(ITEM_DELETE_TAGS), itemId);
        } catch (DataAccessException e) {
            log.error("Query fails by delete item's tags by item id: '{}'", itemId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        log.debug("Deleting tags by item id '{}' successful", itemId);
    }

    private int getItemNumberOfUsers(int itemId) {
        log.debug("Try to find item number of users by item id: '{}'", itemId);
        try {
            return jdbcTemplate.queryForObject(
                    env.getProperty(ITEM_GET_NUMBER_OF_ITEM_USERS), new Object[]{itemId}, Integer.class);
        } catch (DataAccessException e) {
            log.error("Query fails by find item number of users by item id: '{}'", itemId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }

    private List<String> getTagsByItemId(int itemId) {
        log.debug("Try to find item tags by item id: '{}'", itemId);
        try {
            return jdbcTemplate.queryForList(env.getProperty(ITEM_GET_TAG_BY_ITEM_ID), new Object[]{itemId}, String.class);
        } catch (DataAccessException e) {
            log.error("Query fails by find item tags by item id: '{}'", itemId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }

    private int getLikeId(int userId, int itemId) {
        log.debug("Try to find like by user id: '{}', item id: '{}'", userId, itemId);
        try {
            List<Integer> list = jdbcTemplate.query(env.getProperty(ITEM_GET_LIKE_ID_BY_USER_ID_ITEM_ID), new Object[]{userId, itemId}, (rs, rowNum) -> rs.getInt(1));
            if (list.isEmpty()) {
                log.debug("Likes by user id: '{}', item id: '{}' not found");
                return 0;
            } else {
                log.debug("Likes by user id: '{}', item id: '{}' was found");
                return list.get(0);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by find like by user id: '{}', item id: '{}'", userId, itemId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }

    public List<String> getLoginsWhoLikedItem(int itemId) {
        log.debug("Try to getUserLikesByItemId for item with id '{}'", itemId);

        List<String> userLogins;
        try {
            userLogins = jdbcTemplate.queryForList(env.getProperty(ITEM_GET_LIKED_USER_LOGINS_BY_ITEM_ID),
                    new Object[]{itemId}, String.class);
        } catch (DataAccessException e) {
            log.error("Query fails by finding login list who liked item with item id '{}'", itemId, e);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (userLogins.isEmpty()) {
            log.debug("Likes for item with id '{}' not found", itemId);
        } else {
            log.debug("Likes for item with id '{}' were found '{}'", itemId, userLogins);
        }
        return userLogins;
    }
}
