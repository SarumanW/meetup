package com.meetup.meetup.security.jwt;

import com.meetup.meetup.exception.runtime.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.meetup.meetup.keys.Key.EXCEPTION_JWT_IS_NOT_CORRECT;


@PropertySource("classpath:strings.properties")
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {
    private static Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Autowired
    private Environment env;

    public JwtAuthFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) {
        log.debug("Trying to get authorization header from http request");

        String authorization = httpServletRequest.getHeader(AUTHORIZATION);

        if (authorization == null || !authorization.startsWith(BEARER)) {
            log.error("Bad header authorization '{}' was received from http request", authorization);
            throw new AuthenticationException(env.getProperty(EXCEPTION_JWT_IS_NOT_CORRECT));
        }

        log.debug("Get authorization header '{}' from http request", authorization);

        String authenticationToken = authorization.replaceAll(BEARER, "");

        log.debug("Trying to create JwtAuthToken with toke '{}'", authenticationToken);

        JwtAuthToken token = new JwtAuthToken(authenticationToken);

        log.debug("Created JwtAuthToken '{}'", token);

        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
