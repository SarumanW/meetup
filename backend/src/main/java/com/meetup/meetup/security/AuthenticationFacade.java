package com.meetup.meetup.security;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.AuthenticationException;
import com.meetup.meetup.security.jwt.JwtAuthenticatedProfile;
import com.meetup.meetup.service.FolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Used for get authenticated {@link User}.
 */
@Component
public class AuthenticationFacade {

    private static Logger log = LoggerFactory.getLogger(AuthenticationFacade.class);

    public User getAuthentication() {
        log.debug("Trying to get authentication from SecurityContextHolder");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("User is not authenticated");
            throw new AuthenticationException("User is not authenticated");
        }

        User authenticatedUser = (User) authentication.getPrincipal();

        log.debug("Found authenticated user '{}'", authenticatedUser);

        return authenticatedUser;
    }
}
