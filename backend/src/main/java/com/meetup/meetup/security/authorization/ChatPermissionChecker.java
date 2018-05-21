package com.meetup.meetup.security.authorization;

import com.meetup.meetup.dao.ChatDao;
import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.entity.Role;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import com.meetup.meetup.service.vm.ChatCheckEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatPermissionChecker {
    private static Logger log = LoggerFactory.getLogger(ChatPermissionChecker.class);

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public boolean checkById(int chatId) {
        log.debug("Check permission for chat with Id '{}'", chatId);

        boolean permission = false;
        User user = authenticationFacade.getAuthentication();

        ChatCheckEntity checkEntity = chatDao.canJoinChat(user.getId(), chatId);


        switch (checkEntity.getRole()) {
            case OWNER:
                if (checkEntity.getChatTypeId() == 1) {
                    permission = true;
                }
                break;
            case PARTICIPANT:
                permission = true;
                break;
        }

        log.info("Chat permission '{}'", permission);

        return permission;
    }

    public boolean checkByEventId(int eventId) {
        log.debug("Check permission for chat with event id'{}'", eventId);

        boolean permission;

        User user = authenticationFacade.getAuthentication();

        Role userRole = eventDao.getRole(user.getId(), eventId);

        log.debug("Get user role from DB '{}'", userRole);

        permission = userRole != Role.NULL;

        log.info("Chat permission '{}'", permission);

        return permission;
    }


}
