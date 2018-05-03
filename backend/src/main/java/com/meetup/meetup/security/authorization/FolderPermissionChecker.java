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

    public boolean canCreateFolder(Folder folder) {
        log.debug("Check permission for create folder '{}'", folder);

        User user = authenticationFacade.getAuthentication();

        boolean permission = folder.getUserId() == user.getId();

        log.info("Create permission '{}'", permission);

        return permission;
    }

    public boolean canUpdateFolder(Folder folder) {
        log.debug("Check permission for update folder '{}'", folder);

        boolean permission = checkPermission(folder.getFolderId());

        log.info("Update permission '{}'", permission);

        return permission;
    }

    public boolean canDeleteFolder(int folderId) {
        log.debug("Check permission for delete folderId '{}'", folderId);

        boolean permission = checkPermission(folderId);

        log.info("Delete permission '{}'", permission);

        return permission;
    }

    private boolean checkPermission(int folderId) {
        User user = authenticationFacade.getAuthentication();

        Folder folderFromDao = folderDao.findById(folderId, user.getId());

        log.debug("Get folder from DB '{}'", folderFromDao);

        return folderFromDao != null;
    }
}
