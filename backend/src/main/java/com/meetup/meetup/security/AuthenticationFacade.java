package com.meetup.meetup.security;

import com.meetup.meetup.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Used for get authenticated {@link User}.
 */
@Component
public class AuthenticationFacade {
    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            return (User) authentication.getPrincipal();
        }

        return null;
    }
}
