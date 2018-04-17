package com.meetup.meetup.service.vm;

import com.meetup.meetup.entity.User;

import java.time.LocalDate;

public class DetailedProfile {
    private String email;
    private String login;
    private String name;
    private String lastname;
    private LocalDate birthDay;
    private String imgPath;

    public DetailedProfile(User user) {
        name = user.getName();
        lastname = user.getLastname();
        email = user.getEmail();
        login = user.getLogin();
        birthDay = user.getBirthDay();
        imgPath = user.getImgPath();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
