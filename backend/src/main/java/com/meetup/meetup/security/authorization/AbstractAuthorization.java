package com.meetup.meetup.security.authorization;

import com.meetup.meetup.security.AuthenticationFacade;

public abstract class AbstractAuthorization {

    protected AuthenticationFacade authenticationFacade;

    public AbstractAuthorization(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    public boolean isUserCorrect(int userId) {
        return userId == authenticationFacade.getAuthentication().getId();
    }
}
