package com.meetup.meetup.service;

import com.meetup.meetup.dao.FolderDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    @Autowired
    private FolderDao folderDao;

    public List<Folder> getUserFolders(int userId){
        return folderDao.getUserFolders(userId);
    }

    public List<Event> getEvents(int folderID){
        return folderDao.getEvents(folderID);
    }

    public Folder addFolder(Folder folder){
        return folderDao.insert(folder);
    }
}
