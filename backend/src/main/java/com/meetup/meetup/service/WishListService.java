package com.meetup.meetup.service;

import com.meetup.meetup.dao.ItemDao;
import com.meetup.meetup.dao.UserDao;
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

import static com.meetup.meetup.keys.Key.EXCEPTION_ENTITY_NOT_FOUND;

@Service
@PropertySource("classpath:strings.properties")
public class WishListService {

    private static Logger log = LoggerFactory.getLogger(WishListService.class);

    private final ItemDao itemDao;
    private final AuthenticationFacade authenticationFacade;
    private final UserDao userDao;
    private final Environment env;

    @Autowired
    public WishListService(ItemDao itemDao, AuthenticationFacade authenticationFacade, UserDao userDao, Environment env) {
        this.itemDao = itemDao;
        this.authenticationFacade = authenticationFacade;
        this.userDao = userDao;
        this.env = env;
    }


    public List<Item> getWishList() {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();

        log.debug("User was successfully received");

        log.debug("Trying to get all WishList for user '{}'", user.toString());
        return itemDao.getWishListByUserId(user.getId());
    }

    public List<Item> getWishesByUser(String login) {
        User user = userDao.findByLogin(login);

        if (user == null) {
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "User", "login", login));
        }

        log.debug("Trying to get wishes from dao by user login '{}'", login);

        return itemDao.getWishListByUserId(user.getId());
    }

    public List<Item> getRecommendations(String[] tagArray) {
        log.debug("Trying to get all recommendations");

        if (tagArray == null) {
            return itemDao.getPopularItems();
        }

        return itemDao.findItemsByTagName(tagArray);
    }

    public List<Item> getBookingByUser() {
        User user = authenticationFacade.getAuthentication();
        log.debug("Trying to get booking wishes from dao by user login '{}'", user.getLogin());
        return itemDao.findBookedItemsByUserId(user.getId());
    }
}
