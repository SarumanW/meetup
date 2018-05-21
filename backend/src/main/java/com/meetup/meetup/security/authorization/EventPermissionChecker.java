package com.meetup.meetup.security.authorization;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventPermissionChecker {

    private static Logger log = LoggerFactory.getLogger(EventPermissionChecker.class);

    @Autowired
    private EventDao eventDao;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private FolderPermissionChecker folderPermissionChecker;

    /**
     * Check if current user can create event
     *
     * @param event entity of event for create
     * @return true if event has owner id as in authenticated user
     * and user has permission for write in folder
     * @see Event
     * @see FolderPermissionChecker
     * @see AuthenticationFacade
     */
    public boolean canCreateEvent(Event event) {
        log.debug("Check permission for update event '{}'", event);

        User user = authenticationFacade.getAuthentication();

        boolean permission = event.getOwnerId() == user.getId() &&
                folderPermissionChecker.checkPermission(event.getFolderId());

        log.info("Create permission '{}'", permission);

        return permission;
    }

    /**
     * Check if current user can edit event
     *
     * @param event entity of event for update
     * @return true if authenticated user is owner of event
     * and user has permission for write in folder
     * @see Event
     * @see FolderPermissionChecker
     * @see AuthenticationFacade
     */
    public boolean checkByEntity(Event event) {
        log.debug("Check permission for edit event by entity '{}'", event);

        boolean permission = checkPermission(event.getEventId()) &&
                folderPermissionChecker.checkPermission(event.getFolderId());

        log.info("Edit permission '{}'", permission);

        return permission;
    }

    /**
     * Check if current user can edit event
     *
     * @param eventId id of event
     * @return true if authenticated user is owner of event
     * @see Event
     * @see AuthenticationFacade
     */
    public boolean checkById(int eventId) {
        log.debug("Check permission for edit event by id'{}'", eventId);

        boolean permission = checkPermission(eventId);

        log.info("Edit permission '{}'", permission);

        return permission;
    }

    private boolean checkPermission(int eventId) {
        User user = authenticationFacade.getAuthentication();

        Role userRole = eventDao.getRole(user.getId(), eventId);

        log.debug("Get user role from DB '{}'", userRole);

        return userRole == Role.OWNER;
    }
}
