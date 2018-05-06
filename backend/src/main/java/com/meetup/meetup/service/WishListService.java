package com.meetup.meetup.service;

import com.meetup.meetup.dao.ItemDao;
import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meetup.meetup.Keys.Key.EXCEPTION_ENTITY_NOT_FOUND;

@Service
@PropertySource("classpath:strings.properties")
public class WishListService {

    private static Logger log = LoggerFactory.getLogger(WishListService.class);

    private final ItemDao itemDao;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    private Environment env;

    @Autowired
    public WishListService(ItemDao itemDao, AuthenticationFacade authenticationFacade) {
        log.info("Initializing WishListService");
        this.itemDao = itemDao;
        this.authenticationFacade = authenticationFacade;
    }

    public List<Item> getUserWishList() {
        log.debug("Trying to get authenticated user");

        User user = authenticationFacade.getAuthentication();

        log.debug("User was successfully received");
        log.debug("Trying to get all WishList for user '{}'", user.toString());


        return itemDao.getUserWishList(user.getId());
    }
    //Check authentication and folder permission
    private void checkPermission(Item item) {
        log.debug("Trying to get user from AuthenticationFacade");

        User user = authenticationFacade.getAuthentication();

        log.debug("User '{}' was successfully received", user.toString());
        log.debug("Trying to check equivalence of item.getUserId '{}' and user.getId '{}'", item.getOwnerId(), user.getId());

        if (item.getOwnerId() != user.getId()) {
            log.error("User has no access to this data");
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"Item", "userId", item.getOwnerId()));
        }

        log.debug("Given access to WishList '{}' for user '{}'", item.toString(), user.toString());
    }
}
