package com.meetup.meetup.service.vm;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RecoveryPasswordProfile {
    private String token;

    @Pattern(regexp = "^[_.@A-Za-z0-9-]*$")
    @Size(min = 6, max = 50)
    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
