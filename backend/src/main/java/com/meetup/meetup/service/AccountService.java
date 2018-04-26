package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.*;
import com.meetup.meetup.security.utils.HashMD5;
import com.meetup.meetup.service.vm.LoginVM;
import com.meetup.meetup.service.vm.RecoveryPasswordVM;
import com.meetup.meetup.service.vm.UserAndTokenVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
public class AccountService {

    private final JwtService jwtService;
    private final UserDao userDao;
    private final MailService mailService;

    @Autowired
    public AccountService(JwtService jwtService, UserDao userDao, MailService mailService) {
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.mailService = mailService;
    }

    public UserAndTokenVM login(LoginVM credentials) throws Exception {
        try {
            String md5Pass = HashMD5.hash(credentials.getPassword());
            credentials.setPassword(md5Pass);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("SendCustomErrorEncoding password");
        }

        User user = userDao.findByLogin((credentials.getLogin()));

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

    public void register(User user) throws Exception {
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
    }

    public void recoveryPasswordMail(String email) throws Exception{
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
    }

    public void recoveryPassword(RecoveryPasswordVM model) throws Exception{
        User user = jwtService.verifyForRecoveryPassword(model.getToken());

        if (user == null) {
            throw new BadTokenException();
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
    }
}
