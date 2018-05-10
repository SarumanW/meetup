package com.meetup.meetup.security.authorization;

import com.meetup.meetup.dao.FolderDao;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "folderPermissionChecker")
public class FolderPermissionChecker {

    private static Logger log = LoggerFactory.getLogger(FolderPermissionChecker.class);

    @Autowired
    private FolderDao folderDao;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    /**
     * Check if current user can create folder
     *
     * @param folder entity of folder for create
     * @return true if authenticated user id is equals with user id in folder
     * @see Folder
     * @see AuthenticationFacade
     */
    public boolean canCreateFolder(Folder folder) {
        log.debug("Check permission for create folder '{}'", folder);

        User user = authenticationFacade.getAuthentication();

        boolean permission = folder.getUserId() == user.getId();

        log.info("Create permission '{}'", permission);

        return permission;
    }

    /**
     * Check if current user can update folder
     *
     * @param folder entity of folder for update
     * @return true if folder exists and authenticated user id is equals with user id in folder
     * @see Folder
     * @see AuthenticationFacade
     */
    public boolean canUpdateFolder(Folder folder) {
        User user = authenticationFacade.getAuthentication();

        boolean permission = checkPermission(folder.getFolderId()) && folder.getUserId() == user.getId();

        log.info("Update permission '{}'", permission);

        return permission;
    }

    /**
     * Check if current user can delete folder
     *
     * @param folderId folder id for delete
     * @return true if folder exists
     * @see Folder
     * @see AuthenticationFacade
     */
    public boolean canDeleteFolder(int folderId) {

        boolean permission = checkPermission(folderId);

        log.info("Delete permission '{}'", permission);

        return permission;
    }

    boolean checkPermission(int folderId) {
        log.debug("Check permission for delete folderId '{}'", folderId);

        User user = authenticationFacade.getAuthentication();

        Folder folderFromDao = folderDao.findById(folderId, user.getId());

        log.debug("Get folder from DB '{}'", folderFromDao);

        return folderFromDao != null;
    }
}
