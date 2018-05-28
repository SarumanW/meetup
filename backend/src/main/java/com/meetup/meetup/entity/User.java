package com.meetup.meetup.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;

    @NotBlank
    @Pattern(regexp = "^[_.@A-Za-z0-9-]*$")
    @Size(min = 4, max = 50)
    private String login;

    @JsonIgnore
    private String password;

    @NotBlank
    @Email
    @Size(min = 5, max = 100)
    private String email;

    @NotBlank
    @Size(min = 4, max = 254)
    private String name;

    @NotBlank
    @Size(min = 4, max = 254)
    private String lastname;

    private String phone;

    private String birthDay;

    @JsonIgnore
    private int timeZone;

    private String imgPath;

    private int pinedEventId;

    private String periodicalEmail;

    private String pinedEventName;
    private String pinedEventDate;

    private String registerDate;
}
