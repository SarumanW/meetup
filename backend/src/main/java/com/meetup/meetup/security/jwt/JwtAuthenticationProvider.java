package com.meetup.meetup.security.jwt;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.AuthenticationException;
import com.meetup.meetup.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.meetup.meetup.keys.Key.EXCEPTION_JWT_AUTHENTICATION;
import static com.meetup.meetup.keys.Key.EXCEPTION_JWT_IS_NOT_CORRECT;

@Component
@PropertySource("classpath:strings.properties")
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Environment env;

    @Override
    public Authentication authenticate(Authentication authentication) {
        log.debug("Try to get token from Authentication");

        String token = (String) authentication.getCredentials();

        if (!StringUtils.hasText(token)) {
            log.error("Failed authentication of user with token '{}'", token);
            throw new AuthenticationException(env.getProperty(EXCEPTION_JWT_IS_NOT_CORRECT));
        }

        log.debug("Try to verify token '{}'", token);

        User user = jwtService.verify(token);

        if (user == null) {
            log.error("Failed authentication. Token '{}' is not verified", token);
            throw new AuthenticationException(env.getProperty(EXCEPTION_JWT_AUTHENTICATION));
        }

        log.debug("Trying to create JwtAuthenticatedProfile with user '{}'", user);

        JwtAuthenticatedProfile authenticatedProfile = new JwtAuthenticatedProfile(user, token);

        log.debug("Created JwtAuthenticatedProfile '{}'", authenticatedProfile);

        return authenticatedProfile;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.equals(authentication);
    }
}
