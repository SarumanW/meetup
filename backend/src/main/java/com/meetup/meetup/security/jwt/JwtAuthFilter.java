package com.meetup.meetup.security.jwt;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtAuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String authorization = request.getHeader("Authorization");
        Authentication authentication3 = SecurityContextHolder.getContext().getAuthentication();

        if (request instanceof SecurityContextHolderAwareRequestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (authorization != null) {
            String token = authorization.replaceAll("Bearer ", "");
            JwtAuthToken jwtAuthToken = new JwtAuthToken(token);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthToken);
        } else {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
