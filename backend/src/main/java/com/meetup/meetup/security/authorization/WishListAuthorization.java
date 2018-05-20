package com.meetup.meetup.security.authorization;

import com.meetup.meetup.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WishListAuthorization extends AbstractAuthorization {

    @Autowired
    public WishListAuthorization(AuthenticationFacade authenticationFacade) {
        super(authenticationFacade);
    }

}
