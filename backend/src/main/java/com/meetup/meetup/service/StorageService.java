package com.meetup.meetup.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.SchemaOutputResolver;

@Service
@PropertySource("classpath:links.properties")
public class StorageService {

    @Autowired
    private Environment env;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserDao userDao;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private Path rootLocation ;//= Paths.get(env.getProperty("profile.img.link"));

    public void store(MultipartFile file) {
        rootLocation = Paths.get(env.getProperty("profile.img.link"));
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            System.out.println("Location exists");
        }
        User user = authenticationFacade.getAuthentication();
        user.setImgPath(env.getProperty("remote.img.link")+user.getId()+".jpg");
        userDao.update(user);
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(user.getId()+".jpg"));
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public void init() {
        rootLocation = Paths.get(env.getProperty("profile.img.link"));
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}