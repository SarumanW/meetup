package com.meetup.meetup.security.jwt;

import com.meetup.meetup.exception.JwtAuthenticationException;
import com.meetup.meetup.service.JwtService;
import com.meetup.meetup.service.vm.MinimalProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            MinimalProfile possibleProfile = jwtService.verify((String) authentication.getCredentials());
            return new JwtAuthenticatedProfile(possibleProfile);
        } catch (Exception e) {
            throw new JwtAuthenticationException("Failed to verify token", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.equals(authentication);
    }
}
