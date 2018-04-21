package com.meetup.meetup.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Getter
    @Setter(onMethod = @__(@JsonIgnore))
    private int id;

    @NotBlank
    @Pattern(regexp = "^[_.@A-Za-z0-9-]*$")
    @Size(min = 4, max = 50)
    private String login;

    @Getter(onMethod = @__(@JsonIgnore))
    @Setter(onMethod = @__({
            @Pattern(regexp = "^[_.@A-Za-z0-9-]*$"),
            @Size(min = 6, max = 50)}))
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

}
