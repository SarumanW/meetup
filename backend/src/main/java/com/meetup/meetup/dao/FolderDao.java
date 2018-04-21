package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;

import java.util.List;


public interface FolderDao extends Dao<Folder> {

    List<Event> getEvents(int folderId);

    List<Folder> getUserFolders(int id);

}
