package com.meetup.meetup.security.jwt;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.JwtAuthenticationException;
import com.meetup.meetup.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String token = (String) authentication.getCredentials();
            User user = jwtService.verify(token);
            JwtAuthenticatedProfile authenticatedProfile = new JwtAuthenticatedProfile(user, token);
            SecurityContextHolder.getContext().setAuthentication(authenticatedProfile);
            return authenticatedProfile;
        } catch (Exception e) {
            throw new JwtAuthenticationException("Failed to verify token", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.equals(authentication);
    }
}
