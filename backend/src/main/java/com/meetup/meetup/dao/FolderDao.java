package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;

import java.util.List;


public interface FolderDao extends Dao<Folder> {

    Folder findById(int id, int userId);

    List<Folder> getUserFolders(int id);

    Folder findByName(String name);

}
