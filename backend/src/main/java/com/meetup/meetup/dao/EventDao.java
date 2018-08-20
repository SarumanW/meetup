package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;

import java.util.List;


public interface EventDao extends Dao<Event> {

    List<Event> findByUserId(int userId);

    List<Event> findByFolderId(int userId, int folderId);

    List<Event> getDrafts(int userId, int folderId);

    List<Event> findByType(int userId, String eventType, int folderId);

    List<User> getParticipants(Event event);

    Role getRole(int userId, int eventId);

    Event createEvent(Event model, int userId);

    void addParticipant(int ownerId, int participantId, int eventId);

    List<Event> getPeriodEvents(int userId, String startDate, String endDate);

    List<Event> getPeriodEventsAllUsers(String startDate, String endDate);

    List<Event> getAllPublic(int userId, String eventName);

    Event pinEvent(int userId, int eventId);

    Event unpinEvent(int userId,int eventId);

    Event deleteParticipants(int ownerId, Event event);

    Event deleteMembers(Event event);

    void deleteParticipant(int ownerId, int eventId, int participantId);

    int unpinAllOnDelete(int eventId);
}
