package com.meetup.meetup.security.authorization;

import com.meetup.meetup.dao.ChatDao;
import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.security.AuthenticationFacade;
import com.meetup.meetup.service.vm.ChatCheckEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatAuthorization extends AbstractAuthorization {
    private static Logger log = LoggerFactory.getLogger(ChatAuthorization.class);

    private final ChatDao chatDao;
    private final EventDao eventDao;

    @Autowired
    public ChatAuthorization(AuthenticationFacade authenticationFacade, ChatDao chatDao, EventDao eventDao) {
        super(authenticationFacade);
        this.chatDao = chatDao;
        this.eventDao = eventDao;
    }

    public boolean checkByChatId(int userId, int chatId) {
        log.debug("Check permission for chat with Id '{}'", chatId);

        ChatCheckEntity checkEntity = chatDao.canJoinChat(userId, chatId);

        if (checkEntity.getRole() == Role.OWNER && checkEntity.getChatTypeId() == 2) {
            log.error("Owner cannot get access to private chat");
            return false;
        }

        log.info("Received ChatCheckEntity '{}' from DB", checkEntity);

        return isUserCorrect(userId) && checkEntity.getRole() != Role.NULL;
    }

    public boolean checkByEventId(int userId, int eventId) {
        log.debug("Check permission for chat with event id'{}'", eventId);

        Role userRole = eventDao.getRole(userId, eventId);

        log.debug("Received user role from DB '{}'", userRole);

        return isUserCorrect(userId) && userRole != Role.NULL;
    }

    public boolean isUserOwnerOfEvent(int userId, int eventId) {
        log.debug("Trying to get roe of user by user id '{}' and event id'{}'", userId, eventId);

        Role userRole = eventDao.getRole(userId, eventId);

        log.debug("Received user '{}' role '{}' by event '{}'", userId, userRole, eventId);

        return isUserCorrect(userId) && userRole == Role.OWNER;
    }
}
