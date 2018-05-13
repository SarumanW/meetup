package com.meetup.meetup.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.meetup.dao.ItemDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.ItemPriority;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@PropertySource("classpath:strings.properties")
public class ItemService {

    private static Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final UserDao userDao;
    private final ItemDao itemDao;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public ItemService(UserDao userDao, ItemDao itemDao, AuthenticationFacade authenticationFacade, Environment env) {
        this.userDao = userDao;
        this.itemDao = itemDao;
        this.authenticationFacade = authenticationFacade;
    }

    public Item findByUserIdItemId(int id, String login) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");
        Item authUserItem = itemDao.findByUserIdItemId(user.getId(), id);

        User userItem = userDao.findByLogin(login);
        log.debug("Trying to get item with id '{}' for user with id '{}'", id, userItem.getId());
        Item requestedUserItem = itemDao.findByUserIdItemId(userItem.getId(),id);
        requestedUserItem.setLike(authUserItem.isLike());
        return requestedUserItem;
    }

    public Item addItem(Item item) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to insert item to database");
        return itemDao.insert(item);
    }

    public Item updateItem(Item item) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to update item '{}' in database", item);
        return itemDao.update(item);
    }

    public Item deleteItem(Item item) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to delete item '{}' from database", item);
        return itemDao.delete(item);
    }

    public Item addItemToUserWishList(Item item) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        item.setOwnerId(user.getId());

        log.debug("Trying to add item with id '{}' in user '{}' wish list", item.getItemId() ,user.getId());

        return itemDao.addToUserWishList(item);
    }

    public Item deleteItemFromUserWishList(int itemId) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to delete item with id '{}' from user '{}' wish list", itemId, user.getId());
        return itemDao.deleteFromUserWishList(user.getId(), itemId);
    }



    public Item addLike(int itemId){
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Try to add like for item with id '{}' for user with id '{}'", itemId, user.getId());
        return itemDao.addLike(itemId, user.getId());
    }

    public Item removeLike(int itemId){
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Try to delete like for item with id '{}' for user with id '{}'", itemId, user.getId());
        return itemDao.removeLike(itemId, user.getId());
    }

    public Item addItemBooker(int ownerId, int itemId, int bookerId) {
        log.debug("Trying to add booker '{}' to item '{}' with owner '{}'", bookerId, itemId, ownerId);
        return itemDao.addBookerForItem(ownerId, itemId, bookerId);
    }

    public Item deleteItemBooker(int ownerId, int itemId) {
        log.debug("Trying to remove booker from item '{}' with owner '{}'", itemId, ownerId);
        return itemDao.removeBookerForItem(ownerId, itemId);
    }

    public List<String> getUserLoginsWhoLikedItem(int itemId){
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Try to get list of login who liked item with id '{}'", itemId);
        return itemDao.getLoginsWhoLikedItem(itemId);
    }
}
