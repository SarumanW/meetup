package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.FolderDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    private static Logger log = LoggerFactory.getLogger(FolderService.class);

    private final FolderDao folderDao;
    private final EventDao eventDao;

    @Autowired
    public FolderService(FolderDao folderDao, EventDao eventDao) {
        log.info("Initializing FolderService");
        this.folderDao = folderDao;
        this.eventDao = eventDao;
    }

    public List<Folder> getUserFolders(int userId) {
        log.debug("Trying to get all folders for user with id '{}'", userId);

        return folderDao.getUserFolders(userId);
    }

    public Folder getFolder(int userId, int folderId){
        return getFolder(userId, folderId, true);
    }

    private Folder getFolder(int userId, int folderId, boolean withEvents) {
        log.debug("Trying to get folder for user with id '{}' by folderId '{}'", userId, folderId);

        Folder folder = folderDao.findById(folderId, userId);

        log.debug("Folder was successfully found '{}'", folder);

        if (withEvents) {
            log.debug("Trying to get events by folderId '{}'", folderId);

            List<Event> events = eventDao.findByFolderId(userId, folderId);
            folder.setEvents(events);

            log.debug("Return folder with events '{}'", folder);
        }

        return folder;
    }

    public Folder addFolder(Folder folder) {
        log.debug("Trying to insert folder to database");

        return folderDao.insert(folder);
    }

    public Folder updateFolder(Folder folder) {
        log.debug("Trying to update folder '{}' in database", folder);

        return folderDao.update(folder);
    }

    public Folder deleteFolder(int userId, int folderId) {
        log.debug("If folder is not general set all events to general");

        Folder folder = getFolder(userId, folderId, false);

        if (!folder.getName().equals("general")) {
            log.debug("Trying set all events from '{}' general folder", folder);

            folderDao.moveEventsToGeneral(folder.getFolderId());

            log.debug("Successful moving all events to general folder");
        }

        log.debug("Trying to delete folder '{}' from database", folder);

        return folderDao.delete(folder);
    }
}
