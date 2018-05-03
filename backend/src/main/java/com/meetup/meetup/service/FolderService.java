package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.FolderDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
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
public class FolderService {

    private static Logger log = LoggerFactory.getLogger(FolderService.class);

    private final FolderDao folderDao;
    private final EventDao eventDao;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    private Environment env;

    @Autowired
    public FolderService(FolderDao folderDao, EventDao eventDao, AuthenticationFacade authenticationFacade) {
        log.info("Initializing FolderService");
        this.folderDao = folderDao;
        this.eventDao = eventDao;
        this.authenticationFacade = authenticationFacade;
    }

    public List<Folder> getUserFolders() {
        log.debug("Trying to get authenticated user");

        User user = authenticationFacade.getAuthentication();

        log.debug("User was successfully received");
        log.debug("Trying to get all folders for user '{}'", user.toString());

        return folderDao.getUserFolders(user.getId());
    }

    public Folder getFolder(int folderId){
        log.debug("Trying to get authenticated user");

        User user = authenticationFacade.getAuthentication();

        log.debug("User was successfully received");
        log.debug("Trying to get folder for user '{}' by folderId '{}'", user.toString(), folderId);

        Folder folder = folderDao.findById(folderId, user.getId());

        if (folder == null) {
            log.error("Folder was not found by folderId '{}' for user '{}'", folderId, user.toString());
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"Folder", "folderId", folderId));
        }

        log.debug("Folder was successfully found");
        log.debug("Trying to get events by folderId '{}'", folderId);

        List<Event> events = eventDao.findByFolderId(folderId);
        folder.setEvents(events);

        log.debug("Return folder with events '{}'", folder.toString());

        return folder;
    }

    public Folder addFolder(Folder folder) {
        log.debug("Try to check permission for folder '{}'", folder.toString());

        checkPermission(folder);

        log.debug("Permission for insert was received");
        log.debug("Trying to insert folder to database");

        return folderDao.insert(folder);
    }

    public Folder deleteFolder(Folder folder) {
        log.debug("Try to check permission for folder '{}'", folder.toString());

        checkPermission(folder);

        log.debug("Permission for delete was received");
        log.debug("If folder is not general set all events to general");

        if (!folder.getName().equals("general")) {
            log.debug("Trying set all events from '{}' general folder", folder.toString());

            folderDao.moveEventsToGeneral(folder.getFolderId());

            log.debug("Successful moving all events to general folder");
        }

        log.debug("Trying to delete folder '{}' from database", folder.toString());

        return folderDao.delete(folder);
    }

    public Folder updateFolder(Folder folder) {
        log.debug("Try to check permission for folder '{}'", folder.toString());

        checkPermission(folder);

        log.debug("Permission for update was received");
        log.debug("Trying to update folder '{}' in database", folder.toString());

        return folderDao.update(folder);
    }

    //Check authentication and folder permission
    private void checkPermission(Folder folder) {
        log.debug("Trying to get user from AuthenticationFacade");

        User user = authenticationFacade.getAuthentication();

        log.debug("User '{}' was successfully received", user.toString());
        log.debug("Trying to check equivalence of folder.getUserId '{}' and user.getId '{}'", folder.getUserId(), user.getId());

        if (folder.getUserId() != user.getId()) {
            log.error("User has no access to this data");
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"Folder", "userId", folder.getUserId()));
        }

        log.debug("Given access to folder '{}' for user '{}'", folder.toString(), user.toString());
    }
}
