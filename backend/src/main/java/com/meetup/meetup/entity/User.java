package com.meetup.meetup.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.meetup.meetup.service.vm.FictUser;

import java.time.LocalDate;

public class User {

    private int id;
    private String login;
    private String password;
    private String email;
    private String phone;
    private String name;
    private String lastName;
    private LocalDate birthDay;
    private int timeZone;
    private String imgPath;

    public User() {
    }

    @Deprecated
    public User(FictUser fictUser) {
        id = fictUser.getId();
        login = fictUser.getLogin();
        password = fictUser.getPassword();
        email = fictUser.getEmail();
        phone = fictUser.getPhone();
        name = fictUser.getName();
        lastName = fictUser.getLastName();
        birthDay = LocalDate.now();
        timeZone = fictUser.getTimeZone();
        imgPath = fictUser.getImgPath();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDay=" + birthDay +
                ", timeZone=" + timeZone +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!login.equals(user.login)) return false;
        if (!email.equals(user.email)) return false;
        if (!name.equals(user.name)) return false;
        if (!lastName.equals(user.lastName)) return false;
        return birthDay != null ? birthDay.equals(user.birthDay) : user.birthDay == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + login.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (birthDay != null ? birthDay.hashCode() : 0);
        return result;
    }
}
