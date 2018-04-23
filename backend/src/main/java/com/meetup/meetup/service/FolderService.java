package com.meetup.meetup.service;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.FolderDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.AuthenticationException;
import com.meetup.meetup.exception.EntityNotFoundException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    private final FolderDao folderDao;
    private final EventDao eventDao;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public FolderService(FolderDao folderDao, EventDao eventDao, AuthenticationFacade authenticationFacade) {
        this.folderDao = folderDao;
        this.eventDao = eventDao;
        this.authenticationFacade = authenticationFacade;
    }

    public List<Folder> getUserFolders() {
        User user = authenticationFacade.getAuthentication();

        return folderDao.getUserFolders(user.getId());
    }

    public Folder getFolder(int folderId){
        User user = authenticationFacade.getAuthentication();

        Folder folder = folderDao.findById(folderId, user.getId());

        if (folder == null) {
            throw new EntityNotFoundException("Folder", "folderId", folderId);
        }

        List<Event> events = eventDao.findByFolderId(folderId);

        folder.setEvents(events);

        return folder;
    }

    public Folder addFolder(Folder folder) {
        checkPermission(folder);

        return folderDao.insert(folder);
    }

    public Folder deleteFolder(Folder folder) {
        checkPermission(folder);

        List<Event> events = eventDao.findByFolderId(folder.getFolderId());
        Folder general = folderDao.findByName("general");

        for(Event e : events){
            e.setFolderId(general.getFolderId());
            eventDao.update(e);
        }

        return folderDao.delete(folder);
    }

    public Folder updateFolder(Folder folder) {
        checkPermission(folder);

        return folderDao.update(folder);
    }

    //Check authentication and folder permission
    private void checkPermission(Folder folder) {
        User user = authenticationFacade.getAuthentication();

        if (folder.getUserId() != user.getId()) {
            throw new EntityNotFoundException("Folder", "userId", folder.getUserId());
        }
    }
}
