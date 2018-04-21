package com.meetup.meetup.service.vm;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.rest.controller.AccountController;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;

/**
 * Used for send {@link User} with token after him authentication {@link AccountController#login(LoginVM loginModel,
        HttpServletResponse response)}.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserAndTokenVM extends User {
    private String token;

    public UserAndTokenVM(User user) {
        this.setId(user.getId());
        this.setLogin(user.getLogin());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        this.setName(user.getName());
        this.setLastname(user.getLastname());
        this.setPhone(user.getPhone());
        this.setBirthDay(user.getBirthDay());
        this.setTimeZone(user.getTimeZone());
        this.setImgPath(user.getImgPath());
    }
}
