package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.*;
import com.meetup.meetup.security.utils.HashMD5;
import com.meetup.meetup.service.vm.LoginVM;
import com.meetup.meetup.service.vm.RecoveryPasswordVM;
import com.meetup.meetup.service.vm.UserAndTokenVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
public class AccountService {

    private static Logger log = LoggerFactory.getLogger(AccountService.class);

    private final JwtService jwtService;
    private final UserDao userDao;
    private final MailService mailService;

    @Autowired
    public AccountService(JwtService jwtService, UserDao userDao, MailService mailService) {
        log.info("Initializing AccountService");
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.mailService = mailService;
    }

    public UserAndTokenVM login(LoginVM credentials) throws Exception {
        log.debug("Trying to get hash from password");

        try {
            String md5Pass = HashMD5.hash(credentials.getPassword());
            credentials.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm can not get hash for password");
            // TODO: 27.04.2018 Create an custom exception
            throw new NoSuchAlgorithmException("SendCustomErrorEncoding password");
        }

        log.debug("Hash for password was successfully get");
        log.debug("Trying get user by login '{}' from dao", credentials.getLogin());

        User user = userDao.findByLogin((credentials.getLogin()));

        log.debug("Check if user with current password exists at database");

        if (user == null || !user.getPassword().equals(credentials.getPassword())) {
            log.error("User login or password is not correct");
            throw new FailedToLoginException(credentials.getLogin());
        }

        log.debug("Login and password is correct for user '{}'", user.toString());
        log.debug("Trying to create token for user");

        String token = jwtService.tokenFor(user);

        if (token == null) {
            log.error("Token was not created for user");
            // TODO: 27.04.2018 Create an custom exception
            throw new Exception("SendCustomErrorEnable to login. Server Error");
        }

        log.debug("Token successfully created for user");
        log.debug("Convert User to UserAndTokenVM class");

        UserAndTokenVM userAndToken = new UserAndTokenVM(user);
        userAndToken.setToken(token);

        log.debug("Returning UserAndTokenVM '{}'", userAndToken.toString());

        return userAndToken;
    }

    public void register(User user) throws Exception {
        log.debug("Trying to get user with login '{}' from database", user.getLogin());

        if (null != userDao.findByLogin(user.getLogin())) {  //checking if user exist in system
            log.error("This login '{}' already exists in database", user.getLogin());
            throw new LoginAlreadyUsedException();
        }

        log.debug("No user found with this login '{}' in database", user.getLogin());
        log.debug("Trying to get user with email '{}' from database", user.getEmail());

        if (null != userDao.findByEmail(user.getEmail())) { //checking if email exist in system
            log.error("This email '{}' already exists in database", user.getEmail());
            throw new EmailAlreadyUsedException();
        }

        log.debug("No user found with this email '{}' in database", user.getEmail());
        log.debug("Trying to hash user password");

        try {
            String md5Pass = HashMD5.hash(user.getPassword());
            user.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm can not create hash for password");
            throw new NoSuchAlgorithmException("SendCustomErrorEncoding password");
        }

        log.debug("Has been given a hashed password to the user");
        log.debug("Trying to insert data about user '{}' in database", user.toString());

        if (userDao.insert(user).getId() == 0) { //checking adding to DB
            log.error("Error caused by inserting user '{}' to database", user.toString());
            throw new DatabaseWorkException();
        }

        log.debug("User data is successfully saved to database");
        log.debug("Trying to send mail for user");
        
        try {
            mailService.sendMailRegistration(user);
        } catch (MailException e) {
            log.error("Letter can not be sent");
            log.debug("Trying delete user from database");
            
            userDao.delete(user);
            e.printStackTrace();
            
            // TODO: 27.04.2018 Create an custom exception
            throw new Exception("SendCustomErrorSend mail exception");
        }

        log.debug("User '{}' is successfully registered in the system", user.toString());
    }

    public void recoveryPasswordMail(String email) throws Exception{
        log.debug("Trying to search user by email '{}'", email);
        
        User user = userDao.findByEmail(email);
        if (user == null) {
            log.error("User was not found by email '{}'", email);
            throw new LoginNotFoundException();
        }

        log.debug("User '{}' was successfully found by email", user.toString());
        log.debug("Trying to create token for password recovery for user '{}'", user.toString());

        String token = jwtService.tokenForRecoveryPassword(user);

        if (token == null) {
            log.error("Token was not created for user");
            // TODO: 27.04.2018 Create an custom exception
            throw new Exception("Token creating error");
        }

        log.debug("Token successfully created for user");
        log.debug("Trying to send message for recovery password on email");

        try {
            mailService.sendMailRecoveryPassword(user, token);
        } catch (MailException e) {
            log.error("Letter can not be sent");
            e.printStackTrace();
            // TODO: 27.04.2018 throw an custom exception
        }

        log.debug("Letter has been sent successfully");
    }

    public void recoveryPassword(RecoveryPasswordVM model) throws Exception{
        log.debug("Trying to verify token '{}' for user", model.getToken());

        User user = jwtService.verifyForRecoveryPassword(model.getToken());

        if (user == null) {
            log.error("Bad token was given at request");
            throw new BadTokenException();
        }

        log.debug("User '{}' was successfully found by token '{}'", user.toString(), model.getToken());
        log.debug("Trying to create hash from password");

        try {
            String md5Pass = HashMD5.hash(model.getPassword());
            user.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm can not create hash for password");
            // TODO: 27.04.2018 Create an custom exception
            throw new NoSuchAlgorithmException("SendCustomErrorEncoding password");
        }

        log.debug("Hash for password was successfully create");
        log.debug("Trying to update user in database");

        if (!userDao.updatePassword(user)) {
            log.error("User was not updated");
            throw new DatabaseWorkException();
        }

        log.debug("Password was successfully updated");
    }
}
