package com.meetup.meetup.service.vm;

import com.meetup.meetup.entity.User;

import javax.validation.constraints.Size;

public class Profile {

    @Size(min = 4, max = 50)
    private String login;

    @Size(min = 6, max = 50)
    private String password;

    private String name;
    private String lastname;
    private String email;
    private String token;
    private String birthDay;
    private String imgPath;

    public Profile(){}

    public Profile(String login, String name, String lastname, String password) {
        this.login = login;
        this.name = name;
        this.lastname= lastname;
        this.password = password;
    }

    public Profile(String name, String lastname, String login) {
        this.name = name;
        this.lastname= lastname;
        this.login = login;
    }

    public Profile(User user) {
        name = user.getName();
        lastname = user.getLastname();
        email = user.getEmail();
        login = user.getLogin();
        birthDay = user.getBirthDay();
        imgPath = user.getImgPath();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
