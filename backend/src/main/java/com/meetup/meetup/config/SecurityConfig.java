package com.meetup.meetup.config;

import com.meetup.meetup.security.jwt.JwtAuthFilter;
import com.meetup.meetup.security.jwt.JwtAuthenticationEntryPoint;
import com.meetup.meetup.security.jwt.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@PropertySource("classpath:strings.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthEndPoint;
    @Autowired
    Environment env;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(env.getProperty("api.loginFull")).permitAll()
                .antMatchers(env.getProperty("api.registerFull")).permitAll()
                .antMatchers(env.getProperty("api.recoveryFull")).permitAll()
                .antMatchers(env.getProperty("api.profileFull")).hasAuthority("ROLE_USER")
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEndPoint);
    }


    //tell Spring Security how to verify the tokens
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }
}