package com.meetup.meetup.rest.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class User {

    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(min = 4, max = 100)
    private String password;

    public User() { }

    public User(@NotBlank @Size(min = 1, max = 50) String username, @Email @Size(min = 5, max = 254) String email, @Size(min = 4, max = 100) String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}