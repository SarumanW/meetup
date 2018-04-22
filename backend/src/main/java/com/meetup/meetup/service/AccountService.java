package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.*;
import com.meetup.meetup.security.utils.HashMD5;
import com.meetup.meetup.service.vm.LoginVM;
import com.meetup.meetup.service.vm.RecoveryPasswordVM;
import com.meetup.meetup.service.vm.UserAndTokenVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
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

    public User login(LoginVM credentials) throws Exception {
        try {
            String md5Pass = HashMD5.hash(credentials.getPassword());
            //credentials.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("SendCustomErrorEncoding password");
        }

        User user = profileService.get(credentials.getLogin());

        if (user == null || !user.getPassword().equals(credentials.getPassword())) {
            throw new FailedToLoginException(credentials.getLogin());
        }

        String token = jwtService.tokenFor(user);

        if (token == null) {
            throw new Exception("SendCustomErrorEnable to login. Server Error");
        }

        UserAndTokenVM userAndToken = new UserAndTokenVM(user);
        userAndToken.setToken(token);
        return userAndToken;
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


        if (userDao.insert(user).getId() == 0) { //checking adding to DB
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

    public ResponseEntity<String> recoveryPasswordMail(String email) throws Exception{
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new LoginNotFoundException();
        }

        String token = jwtService.tokenForRecoveryPassword(user);

        if (token == null) {
            throw new Exception("Token creating error");
        }

        try {
            mailService.sendMailRecoveryPassword(user, token);
        } catch (MailException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("{ \"success\" : \"Success\" }", HttpStatus.OK);
    }

    public ResponseEntity<String> recoveryPassword(RecoveryPasswordVM model) throws Exception{
        String login = jwtService.verifyLogin(model.getToken());
        if (login == null) {
            throw new BadTokenException();
        }

        User user = profileService.get(login);
        if (user == null) {
            throw new LoginNotFoundException();
        }

        try {
            String md5Pass = HashMD5.hash(model.getPassword());
            user.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("SendCustomErrorEncoding password");
        }

        if (!userDao.updatePassword(user)) {
            throw new DatabaseWorkException();
        }
        return new ResponseEntity<>("{ \"success\" : \"Success\" }", HttpStatus.ACCEPTED);
    }
}
