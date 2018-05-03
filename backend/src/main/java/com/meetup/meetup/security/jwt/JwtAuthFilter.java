package com.meetup.meetup.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtAuthFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof SecurityContextHolderAwareRequestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        log.debug("Trying to get header Authorization from request");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authorization = request.getHeader("Authorization");

        log.debug("Authorization '{}'", authorization);

        if (authorization != null) {
            log.debug("Create new instance of JwtAuthToken and set it to SecurityContextHolder");

            String token = authorization.replaceAll("Bearer ", "");
            JwtAuthToken jwtAuthToken = new JwtAuthToken(token);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthToken);
        } else {
            log.debug("Clear SecurityContextHolder");

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
