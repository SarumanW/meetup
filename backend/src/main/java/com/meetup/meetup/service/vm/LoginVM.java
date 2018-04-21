package com.meetup.meetup.service.vm;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.rest.controller.AccountController;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Used for get {@link User} credentials at
 * {@link AccountController#login(LoginVM, HttpServletResponse)}.
 */
@Data
public class LoginVM {
    @NotBlank
    @Size(min = 4, max = 50)
    private String login;

    @Size(min = 6, max = 50)
    private String password;
}
