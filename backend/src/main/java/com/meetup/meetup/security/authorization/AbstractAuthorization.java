package com.meetup.meetup.security.authorization;

import com.meetup.meetup.security.AuthenticationFacade;

public class AbstractAuthorization {

    protected AuthenticationFacade authenticationFacade;

    public AbstractAuthorization(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    public boolean isUserCorrect(int userId) {
        return userId == authenticationFacade.getAuthentication().getId();
    }
}
