package com.meetup.meetup.security.authorization;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventPermissionChecker {

    private static Logger log = LoggerFactory.getLogger(FolderPermissionChecker.class);

    @Autowired
    private EventDao eventDao;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public boolean canUpdateEvent(Event event) {
        log.debug("Check permission for update event '{}'", event);

        boolean permission = checkPermission(event.getEventId());

        log.info("Update permission '{}'", permission);

        return permission;
    }

    public boolean canDeleteEvent(int eventId) {
        log.debug("Check permission for delete event '{}'", eventId);

        boolean permission = checkPermission(eventId);

        log.info("Delete permission '{}'", permission);

        return permission;
    }

    private boolean checkPermission(int eventId) {
        User user = authenticationFacade.getAuthentication();

        Role userRole = eventDao.getRole(user.getId(), eventId);

        log.debug("Get user role from DB '{}'", userRole);

        return userRole == Role.OWNER;
    }
}
