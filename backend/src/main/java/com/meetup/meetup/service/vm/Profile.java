package com.meetup.meetup.service.vm;

import com.meetup.meetup.entity.User;
import lombok.Data;

import javax.validation.constraints.Size;


@Data
public class Profile {

    private int id;

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


    public Profile(String login, String name, String lastname, String password) {
        this(name, lastname, login);
        this.password = password;
    }

    public Profile(String name, String lastname, String login) {
        this.name = name;
        this.lastname= lastname;
        this.login = login;
    }

    public Profile(User user) {
        id = user.getId();
        name = user.getName();
        lastname = user.getLastname();
        email = user.getEmail();
        login = user.getLogin();
        birthDay = user.getBirthDay();
        imgPath = user.getImgPath();
    }

    public User getUser(User user){
        user.setName(this.name);
        user.setLastname(this.lastname);
        user.setEmail(this.email);
        user.setLogin(this.login);
        user.setBirthDay(this.birthDay);
        return user;
    }

}