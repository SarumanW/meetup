package com.meetup.meetup.service.vm;

import com.meetup.meetup.entity.User;

public class MinimalProfile {
    private String login;
    private String name;
    private String lastname;
    private String token;

    public MinimalProfile(User user) {
        name = user.getName();
        lastname = user.getLastName();
        login = user.getLogin();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
