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

import static com.meetup.meetup.Keys.Key.EXCEPTION_ENTITY_NOT_FOUND;

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
        this.env = env;
    }

    private final Environment env;

    public Item getItemById(int id) {

        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to get item with id '{}'", id);
        Item item = itemDao.findById(id);

        if (item == null) {
            log.error("Item with id '{}' wasnt founded", id);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "Item", "itemId", id));
        }

        return item;
    }
}
