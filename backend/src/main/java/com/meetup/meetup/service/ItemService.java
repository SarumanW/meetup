package com.meetup.meetup.service;


import com.meetup.meetup.dao.ItemDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private static Logger log = LoggerFactory.getLogger(ItemService.class);

    private final UserDao userDao;
    private final ItemDao itemDao;

    @Autowired
    public ItemService(UserDao userDao, ItemDao itemDao) {
        this.userDao = userDao;
        this.itemDao = itemDao;
    }

    public Item findByUserIdItemId(int itemId, String ownerLogin) {
        log.debug("Trying to insert item to database");

        if(ownerLogin.equals("no")) {
            return itemDao.findById(itemId);
        }

        User owner = userDao.findByLogin(ownerLogin);

        return itemDao.findByUserIdItemId(owner.getId(), itemId);
    }

    public Item addItem(Item item) {
        log.debug("Trying to insert item to database");

        return itemDao.insert(item);
    }

    public Item updateItem(Item item) {
        log.debug("Trying to update item '{}' in database", item);

        return itemDao.update(item);
    }

    public Item deleteItem(Item item) {
        log.debug("Trying to delete item '{}' from database", item);

        return itemDao.delete(item);
    }

    public Item addItemToUserWishList(int userId, Item item) {
        log.debug("Trying to add item with id '{}' in user '{}' wish list", item.getItemId(), userId);

        return itemDao.addToUserWishList(item);
    }

    public Item deleteItemFromUserWishList(int userId, int itemId) {
        log.debug("Trying to delete item with id '{}' from user '{}' wish list", itemId, userId);

        return itemDao.deleteFromUserWishList(userId, itemId);
    }

    public Item addLike(int userId, int itemId){
        log.debug("Try to add like for item with id '{}' for user with id '{}'", itemId, userId);

        return itemDao.addLike(itemId, userId);
    }

    public Item removeLike(int userId, int itemId){
        log.debug("Try to delete like for item with id '{}' for user with id '{}'", itemId, userId);

        return itemDao.removeLike(itemId, userId);
    }

    public Item addItemBooker(int ownerId, int itemId, int bookerId) {
        log.debug("Trying to add booker '{}' to item '{}' with owner '{}'", bookerId, itemId, ownerId);

        return itemDao.addBookerForItem(ownerId, itemId, bookerId);
    }

    public Item deleteItemBooker(int ownerId, int itemId, int bookerId) {
        log.debug("Trying to remove booker from item '{}' with owner '{}'", itemId, ownerId);

        return itemDao.removeBookerForItem(ownerId, itemId, bookerId);
    }

    public List<String> getUserLoginsWhoLikedItem(int itemId) {
        log.debug("Try to get list of login who liked item with id '{}'", itemId);

        return itemDao.getLoginsWhoLikedItem(itemId);
    }
}
