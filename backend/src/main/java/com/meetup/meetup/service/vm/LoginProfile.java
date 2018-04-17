package com.meetup.meetup.service.vm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginProfile {

    @NotBlank
    @Size(min = 4, max = 50)
    private String login;

    @Size(min = 6, max = 50)
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
