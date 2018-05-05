package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;

import java.util.List;


public interface EventDao extends Dao<Event> {

    List<Event> findByUserId(int userId);

    List<Event> findByFolderId(int folderId);

    List<Event> getDrafts(int folderId);

    List<Event> findByType(String eventType, int folderId);

    List<User> getParticipants(Event event);

    Role getRole(int userId, int eventId);

    Event createEvent(Event model, int userId);

    void addParticipant(int participantId, int eventId);

    Event deleteParticipants(Event event);

    Event deleteMembers(Event event);
}
