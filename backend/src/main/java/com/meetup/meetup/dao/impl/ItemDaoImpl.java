package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.ItemDao;
import com.meetup.meetup.dao.rowMappers.ItemRowMapper;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.ItemPriority;
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
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.*;

import static com.meetup.meetup.keys.Key.*;

@Repository
@PropertySource("classpath:sqlDao.properties")
@PropertySource("classpath:strings.properties")
@PropertySource("classpath:image.properties")
public class ItemDaoImpl implements ItemDao {

    private static Logger log = LoggerFactory.getLogger(ItemDaoImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        } catch (EmptyResultDataAccessException e) {
            log.debug("Item personal info not found by user id: '{}' and item id: '{}'", userId, itemId);
            return item;
        }catch (DataAccessException e) {
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
        } catch (EmptyResultDataAccessException e) {
            log.debug("Item not found by item id: '{}'", itemId);
            return null;
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

        return items;
    }

    @Override
    public Item addToUserWishList(Item item) {
        log.debug("Try add to wish list by user id: '{}', item id: '{}', priority: '{}'",
                item.getOwnerId(), item.getItemId(), item.getPriority());
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
        } catch (DataAccessException e) {
            log.error("Query fails by add item to wish list by user id: '{}', item id: '{}', priority: '{}'",
                    item.getOwnerId(), item.getItemId(), item.getPriority());
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
        return findByUserIdItemId(ownerId, itemId);
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

            int result = insertItem.execute(itemParameters);

            if (result != 0) {
                log.debug("Like by item id: '{}', user id: '{}' was added", itemId, userId);
            } else {
                log.debug("Like by item id: '{}', user id: '{}' was not added", itemId, userId);
            }
        } catch (DataAccessException e) {
            log.error("Query fails by add like by item id: '{}', user id: '{}'", itemId, userId);
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
            log.error("Query fails by remove like by item id: '{}', user id: '{}'", itemId, userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return findByUserIdItemId(userId, itemId);
    }

    @Override
    public List<Item> getPopularItems(String[] tagArray) {
        return null;
    }

    @Override
    public List<Item> findBookedItemsByUserId(int userId) {
        log.debug("Try get booked items list by user id: '{}'", userId);
        List<Item> items = new ArrayList<>();
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
                        item.setLike(getLikeId(userId, item.getItemId()) != 0);
                        item.setTags(getTagsByItemId(item.getItemId()));
                        return item;
                    });
        } catch (EmptyResultDataAccessException e) {
            log.debug("Booked items not found by user id: '{}'", userId);
            return items;
        } catch (DataAccessException e) {
            log.error("Query fails by find item by user id: '{}' and item id: '{}'", userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return items;
    }

    @Override
    public List<Item> findItemsByTagName(String[] tagNames) {
        log.debug("Try get items list by tag name: '{}'", Arrays.toString(tagNames));

        List<Item> items = new ArrayList<>();
        getItemsIdByTagName(tagNames).forEach((itemId) -> items.add(findById(itemId)));

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
                int result = jdbcTemplate.update(env.getProperty(ITEM_DELETE), model.getItemId());
                if(result == 0){
                    log.error("Not deleted item with item id: '{}'", model.getItemId());
                }else{
                    log.error("Successfully deleted item with item id: '{}'", model.getItemId());
                }
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
        } catch (EmptyResultDataAccessException e) {
            log.debug("Searched tags not found by aboutTag: '{}'", aboutTag);
            return new ArrayList<>();
        } catch (DataAccessException e) {
            log.error("Query fails by searching tags name by about tag: '{}'", aboutTag);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }

    // TODO: 10.05.2018 refactor it !!!
    private List<Integer> getItemsIdByTagName(String[] tagNames) {
        log.debug("Try to get items id by tag name: '{}'", Arrays.toString(tagNames));

        StringBuilder builder = new StringBuilder("SELECT ITEM_ID" +
                "  FROM TAG_ITEM " +
                "  INNER JOIN TAG T ON TAG_ITEM.TAG_ID = T.TAG_ID " +
                "  WHERE NAME IN (?");

        for (int i = 1; i < tagNames.length; i++) {
            builder.append(",?");
        }
        String ps = builder.append(") GROUP BY ITEM_ID HAVING COUNT(ITEM_ID) = ?").toString();
        List<Object> params = new ArrayList<>(Arrays.asList(tagNames));
        params.add(tagNames.length);

        List<Integer> itemsIds = new ArrayList<>();
        try {
            itemsIds = jdbcTemplate.queryForList(
                    ps, params.toArray(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Items id not found by tag names: '{}'", Arrays.toString(tagNames));
            return itemsIds;
        } catch (DataAccessException e) {
            log.error("Query fails by finding item's ids with tag name: '{}'", Arrays.toString(tagNames));
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return itemsIds;
    }

    private List<Integer> getUserItemsId(int userId) {
        log.debug("Try to getUserItemsIds by user id '{}'", userId);

        List<Integer> itemsIds = new ArrayList<>();
        try {
            itemsIds = jdbcTemplate.queryForList(env.getProperty(ITEM_GET_ITEMS_ID_BY_USER_ID),
                    new Object[]{userId}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Items id not found by user id: '{}'", userId);
            return itemsIds;
        } catch (DataAccessException e) {
            log.error("Query fails by finding user item's ids with user id '{}'", userId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return itemsIds;
    }

    private List<Integer> getPopularItemsId() {
        log.debug("Try to get popular item's id");

        List<Integer> itemsIds = new ArrayList<>();
        try {
            itemsIds = jdbcTemplate.queryForList(
                    env.getProperty(ITEM_GET_POPULAR_ITEMS_ID), new Object[]{numberOfPopularItem}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Popular items id not found");
            return itemsIds;
        } catch (DataAccessException e) {
            log.error("Query fails by finding popular item's ids");
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
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
        } catch (EmptyResultDataAccessException e) {
            log.debug("Tag names not found by item id: '{}'", itemId);
            return new ArrayList<>();
        } catch (DataAccessException e) {
            log.error("Query fails by find item tags by item id: '{}'", itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
    }

    private int getLikeId(int userId, int itemId){
        log.debug("Try to find like by user id: '{}', item id: '{}'", userId, itemId);
        try {
            return jdbcTemplate.queryForObject(env.getProperty(ITEM_GET_LIKE_ID_BY_USER_ID_ITEM_ID), new Object[]{userId, itemId}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Like not found by by user id: '{}', item id: '{}'", userId, itemId);
            return 0;
        } catch (DataAccessException e) {
            log.error("Query fails by find like by user id: '{}', item id: '{}'", userId, itemId);
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
            log.error("Query fails by finding login list who liked item with item id '{}'", itemId);
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

