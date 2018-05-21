package com.meetup.meetup.service.vm;

import com.meetup.meetup.rest.controller.AccountController;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Used for recovery password at {@link AccountController#passwordRecovery(RecoveryPasswordVM)}.
 */
@Data
public class RecoveryPasswordVM {
    private String token;

    @NotBlank
    @Pattern(regexp = "^[_.@A-Za-z0-9-]*$")
    @Size(min = 6, max = 50)
    private String password;
}
