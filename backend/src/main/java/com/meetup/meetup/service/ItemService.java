package com.meetup.meetup.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.meetup.dao.ItemDao;
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

@Service
@PropertySource("classpath:strings.properties")
public class ItemService {

    private static Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final ItemDao itemDao;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public ItemService(ItemDao itemDao, AuthenticationFacade authenticationFacade, Environment env) {
        this.itemDao = itemDao;
        this.authenticationFacade = authenticationFacade;
    }

    public Item getItemById(int id) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to get item with id '{}'", id);
        return itemDao.findByUserIdItemId(user.getId(),id);
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

    //todo who can update and delete item??
    private void checkPermission(Item item) {
//        log.debug("Trying to get user from AuthenticationFacade");
//        User user = authenticationFacade.getAuthentication();
//        log.debug("User '{}' was successfully received", user.toString());
//
//        log.debug("Trying to check equivalence of item.getBookerId '{}' and user.getId '{}'", item.getBookerId(), user.getId());
//        if (item.getBookerId() != user.getId()) {
//            log.error("User has no access to this data");
//            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"Item", "userId", item.getBookerId()));
//        }
//
//        log.debug("Given access to item '{}' for user '{}'", item, user);
    }

    public Item addItemToUserWishList(int itemId, Item item) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to add item with id '{}' in user '{}' wish list", itemId, user.getId());
        return itemDao.addToUserWishList(user.getId(), itemId, item.getPriority());
    }

    public Item deleteItemFromUserWishList(int itemId) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to delete item with id '{}' from user '{}' wish list", itemId, user.getId());
        return itemDao.deleteFromUserWishList(user.getId(), itemId);
    }


// TODO: 08.05.2018 add check likes
//    public Item addLike(int itemId){
//        log.debug("Trying to get authenticated user");
//        User user = authenticationFacade.getAuthentication();
//        log.debug("User was successfully received");
//
//        log.debug("Try to add like for item with id '{}'", itemId);
//        return itemDao.addLike(itemId, user.getId());
//    }
//
//    public Item removeLike(int itemId){
//        log.debug("Trying to get authenticated user");
//        User user = authenticationFacade.getAuthentication();
//        log.debug("User was successfully received");
//
//        log.debug("Try to delete like for item with id '{}'", itemId);
//        return itemDao.removeLike(itemId, user.getId());
//    }
}
