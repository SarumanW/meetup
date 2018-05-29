package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.BadTokenException;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
import com.meetup.meetup.exception.runtime.HashAlgorithmException;
import com.meetup.meetup.exception.runtime.NoTokenException;
import com.meetup.meetup.exception.runtime.frontend.detailed.*;
import com.meetup.meetup.security.AuthenticationFacade;
import com.meetup.meetup.security.utils.HashMD5;
import com.meetup.meetup.service.vm.LoginVM;
import com.meetup.meetup.service.vm.RecoveryPasswordVM;
import com.meetup.meetup.service.vm.UserAndTokenVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import static com.meetup.meetup.keys.Key.*;

@Component
@PropertySource("classpath:strings.properties")
public class AccountService {

    private static Logger log = LoggerFactory.getLogger(AccountService.class);

    private final JwtService jwtService;
    private final UserDao userDao;
    private final MailService mailService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    private Environment env;

    @Autowired
    public AccountService(JwtService jwtService, UserDao userDao, MailService mailService, AuthenticationFacade authenticationFacade) {
        log.info("Initializing AccountService");
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.mailService = mailService;
        this.authenticationFacade = authenticationFacade;
    }

    public UserAndTokenVM login(LoginVM credentials) {
        log.debug("Trying to get hash from password");

        try {
            String md5Pass = HashMD5.hash(credentials.getPassword());
            credentials.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm can not get hash for password", e);
            throw new HashAlgorithmException(env.getProperty(EXCEPTION_HASH_ALGORITHM));
        }

        log.debug("Hash for password was successfully get");
        log.debug("Trying get user by login '{}' from dao", credentials.getLogin());

        User user = userDao.findByLogin((credentials.getLogin()));

        log.debug("Check if user with current password exists at database");

        if (user == null || !user.getPassword().equals(credentials.getPassword())) {
            log.error("User login or password is not correct");
            throw new FailedToLoginException(String.format(env.getProperty(EXCEPTION_FAILED_LOGIN), credentials.getLogin()));
        }

        log.debug("Login and password is correct for user '{}'", user.toString());
        log.debug("Trying to create token for user");

        String token = jwtService.tokenFor(user);

        if (token == null) {
            log.error("Token was not created for user");
            throw new NoTokenException(env.getProperty(EXCEPTION_NO_TOKEN));
        }

        log.debug("Token successfully created for user");
        log.debug("Convert User to UserAndTokenVM class");

        UserAndTokenVM userAndToken = new UserAndTokenVM(user);
        userAndToken.setToken(token);

        log.debug("Returning UserAndTokenVM '{}'", userAndToken.toString());

        return userAndToken;
    }

    @Transactional
    public void register(User user) {
        log.debug("Trying to get user with login '{}' from database", user.getLogin());

        if (!userDao.isLoginFree(user.getLogin())) {  //checking if user exist in system
            log.error("This login '{}' already exists in database", user.getLogin());
            throw new LoginAlreadyUsedException(env.getProperty(EXCEPTION_LOGIN_USED));
        }

        log.debug("No user found with this login '{}' in database", user.getLogin());
        log.debug("Trying to get user with email '{}' from database", user.getEmail());

        if (!userDao.isEmailFree(user.getEmail())) { //checking if email exist in system
            log.error("This email '{}' already exists in database", user.getEmail());
            throw new EmailAlreadyUsedException(env.getProperty(EXCEPTION_EMAIL_USED));
        }

        log.debug("No user found with this email '{}' in database", user.getEmail());

        log.debug("Trying to send mail for user");

        mailService.sendMailConfirmationRegistration(user, jwtService.tokenForConfirmationRegistration(user));

        log.debug("Add register date to user entity");

        user.setRegisterDate(new Timestamp(System.currentTimeMillis()).toString());

        log.debug("Trying to insert data about user '{}' in database", user.toString());

        if (userDao.insert(user).getId() == 0) { //checking adding to DB
            log.error("Error caused by inserting user '{}' to database", user.toString());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        log.debug("User '{}' is successfully registered in the system", user.toString());
    }

    public void confirmRegistration(RecoveryPasswordVM model) {
        User user = recoveryPassword(model);

        log.debug("Trying to send mail for user about successful registration");

        mailService.sendMailSuccessfulRegistration(user);

        log.debug("User '{}' is successfully registered in the system", user.toString());
    }

    public void recoveryPasswordMail(String email) {
        log.debug("Trying to search user by email '{}'", email);

        User user = userDao.findByEmail(email);
        if (user == null) {
            log.error("User was not found by email '{}'", email);
            throw new EmailNotFoundException(env.getProperty(EXCEPTION_EMAIL_NOT_FOUND));
        }

        log.debug("User '{}' was successfully found by email", user.toString());
        log.debug("Trying to create token for password recovery for user '{}'", user.toString());

        String token = jwtService.tokenForRecoveryPassword(user);

        if (token == null) {
            log.error("Token was not created for user");
            throw new NoTokenException(env.getProperty(EXCEPTION_NO_TOKEN));
        }

        log.debug("Token successfully created for user");
        log.debug("Trying to send message for recovery password on email");

        mailService.sendMailRecoveryPassword(user, token);

        log.debug("Letter has been sent successfully");
    }

    private User recoveryPassword(RecoveryPasswordVM model) {
        log.debug("Trying to verify token '{}' for user", model.getToken());

        User user = jwtService.verifyForRecoveryPassword(model.getToken());

        if (user == null) {
            log.error("Bad token was given at request");
            throw new BadTokenException(env.getProperty(EXCEPTION_BAD_TOKEN));
        }

        log.debug("User '{}' was successfully found by token '{}'", user.toString(), model.getToken());
        log.debug("Trying to create hash from password");

        try {
            String md5Pass = HashMD5.hash(model.getPassword());
            user.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm can not create hash for password", e);
            throw new HashAlgorithmException(env.getProperty(EXCEPTION_HASH_ALGORITHM));
        }

        log.debug("Hash for password was successfully create");
        log.debug("Trying to update user in database");

        userDao.updatePassword(user);

        log.debug("Password was successfully updated");

        return user;
    }

    public void checkPassword(RecoveryPasswordVM model) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();

        if (user == null) {
            log.error("Bad token was given at request");
            throw new BadTokenException(env.getProperty(EXCEPTION_BAD_TOKEN));
        }

        log.debug("User '{}' was successfully found by token '{}'", user.toString(), model.getToken());
        log.debug("Trying to get hash from password");

        try {
            String md5Pass = HashMD5.hash(model.getPassword());
            model.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm can not get hash for password", e);
            throw new HashAlgorithmException(env.getProperty(EXCEPTION_HASH_ALGORITHM));
        }

        log.debug("Hash for password was successfully get");
        log.debug("Check if user with current password exists at database");

        if (!user.getPassword().equals(model.getPassword())) {
            log.error("User password is not correct");
            throw new FailedToLoginException(env.getProperty(EXCEPTION_FAILED_LOGIN));
        }

        log.debug("Password is correct for user '{}'", user.toString());
    }

    public void changePassword(RecoveryPasswordVM model) {

        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();

        if (user == null) {
            log.error("Bad token was given at request");
            throw new BadTokenException(env.getProperty(EXCEPTION_BAD_TOKEN));
        }

        log.debug("User '{}' was successfully found by token '{}'", user.toString(), model.getToken());
        log.debug("Trying to create hash from password");

        try {
            String md5Pass = HashMD5.hash(model.getPassword());
            user.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm can not create hash for password", e);
            throw new HashAlgorithmException(env.getProperty(EXCEPTION_HASH_ALGORITHM));
        }

        log.debug("Hash for password was successfully create");
        log.debug("Trying to update user in database");

        userDao.updatePassword(user);

        log.debug("Password was successfully updated");
    }

}
