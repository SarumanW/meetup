package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.rest.controller.errors.DatabaseWorkException;
import com.meetup.meetup.rest.controller.errors.EmailAlreadyUsedException;
import com.meetup.meetup.rest.controller.errors.FailedToLoginException;
import com.meetup.meetup.rest.controller.errors.LoginAlreadyUsedException;
import com.meetup.meetup.security.utils.HashMD5;
import com.meetup.meetup.service.vm.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import javax.json.Json;
import java.security.NoSuchAlgorithmException;

@Component
public class AccountService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MailService mailService;

    public Profile login(Profile credentials) throws Exception {
        try {
            String md5Pass = HashMD5.hash(credentials.getPassword());
            credentials.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("SendCustomErrorEncoding password");
        }

        User user = userDao.findByLogin(credentials.getLogin());

        Profile minimalProfile;

        if (user != null && user.getPassword().equals(credentials.getPassword())) {
            minimalProfile = new Profile(user.getLogin(), user.getName(), user.getLastname(), user.getPassword());
        } else {
            throw new FailedToLoginException(credentials.getLogin());
        }

        String token;

        token = jwtService.tokenFor(minimalProfile);

        if (token == null) {
            throw new Exception("SendCustomErrorEnable to login. Server Error");
        }
        minimalProfile.setToken(token);

        return minimalProfile;
    }

    public String register(User user) throws Exception {
        if (null != userDao.findByLogin(user.getLogin())) {  //checking if user exist in system
            throw new LoginAlreadyUsedException();
        }

        if (null != userDao.findByEmail(user.getEmail())) { //checking if email exist in system
            throw new EmailAlreadyUsedException();
        }

        try {
            String md5Pass = HashMD5.hash(user.getPassword());
            user.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("SendCustomErrorEncoding password");
        }


        if (userDao.insert(user) == -1) { //checking adding to DB
            throw new DatabaseWorkException();
        }

        try {
            mailService.sendMailRegistration(user);
        } catch (MailException e) {
            userDao.delete(user);
            e.printStackTrace();
            throw new Exception("SendCustomErrorSend mail exception");
        }

        return Json.createObjectBuilder().add("success", "Success").build().toString();
    }
}
