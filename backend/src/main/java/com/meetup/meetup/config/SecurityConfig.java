package com.meetup.meetup.config;

import com.meetup.meetup.security.jwt.JwtAuthFilter;
import com.meetup.meetup.security.jwt.JwtAuthenticationEntryPoint;
import com.meetup.meetup.security.jwt.JwtAuthenticationProvider;
import com.meetup.meetup.security.jwt.JwtSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

import static com.meetup.meetup.keys.Key.URL_API_PATTERN;
import static com.meetup.meetup.keys.Key.URL_AUTH_PATTERN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@PropertySource("classpath:strings.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthEndPoint;
    @Autowired
    private Environment env;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

    @Bean
    public JwtAuthFilter authenticationTokenFilter() {
        JwtAuthFilter filter = new JwtAuthFilter(env.getProperty(URL_API_PATTERN));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEndPoint);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers(env.getProperty(URL_AUTH_PATTERN)).permitAll()
                .antMatchers(env.getProperty(URL_API_PATTERN)).authenticated();
    }
}
