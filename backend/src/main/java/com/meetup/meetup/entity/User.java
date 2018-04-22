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

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Pattern(regexp = "^[_.@A-Za-z0-9-]*$")
    @Size(min = 6, max = 50)
    private String password;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(min = 4, max = 254)
    private String name;

    @Size(min = 4, max = 254)
    private String lastname;

    @JsonIgnore
    private String phone;

    @JsonIgnore
    private String birthDay;

    @JsonIgnore
    private int timeZone;

    @JsonIgnore
    private String imgPath;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
