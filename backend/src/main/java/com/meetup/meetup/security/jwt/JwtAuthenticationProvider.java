package com.meetup.meetup.security.jwt;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.JwtAuthenticationException;
import com.meetup.meetup.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.meetup.meetup.Keys.Key.EXCEPTION_JWT_AUTHENTICATION;

@Component
@PropertySource("classpath:strings.properties")
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Environment env;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("Try to get token from Authentication");

        String token = (String) authentication.getCredentials();
        try {
            log.debug("Try to verify token '{}'", token);

            User user = jwtService.verify(token);

            log.debug("Creating JwtAuthenticatedProfile with user '{}' and set it to SecurityContextHolder", user);

            JwtAuthenticatedProfile authenticatedProfile = new JwtAuthenticatedProfile(user, token);
            SecurityContextHolder.getContext().setAuthentication(authenticatedProfile);
            return authenticatedProfile;
        } catch (Exception e) {
            log.error("Failed authentication of user with token '{}'", token);
            throw new JwtAuthenticationException(env.getProperty(EXCEPTION_JWT_AUTHENTICATION), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.equals(authentication);
    }
}
