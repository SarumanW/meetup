package com.meetup.meetup.service;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;

import com.meetup.meetup.exception.runtime.frontend.detailed.FileUploadException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import static com.meetup.meetup.Keys.Key.EXCEPTION_FILE_UPLOAD;


@Service
@PropertySource("classpath:links.properties")
@PropertySource("classpath:strings.properties")
public class StorageService {

    @Autowired
    private Environment env;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserDao userDao;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private Path rootLocation;

    public User store(MultipartFile file) {
        log.debug("Try to receive img");
        rootLocation = Paths.get(env.getProperty("profile.img.link"));
        log.debug("Try to receive img to path: '{}'", rootLocation.getFileName());
        User user = authenticationFacade.getAuthentication();

        String inFileFormat = "." + file.getOriginalFilename().split("\\.")[1];
        log.debug("File format is {}", inFileFormat);
        user.setImgPath(env.getProperty("remote.img.link") + user.getId() + inFileFormat);
        log.debug("Update user's image path to '{}'", user.getImgPath());
        userDao.update(user);
        log.debug("User's image path is updated");
        try {
            log.debug("Copying file");
            Files.deleteIfExists(this.rootLocation.resolve(user.getId() + inFileFormat));
            Files.copy(file.getInputStream(), this.rootLocation.resolve(user.getId() + inFileFormat));
            log.debug("Copying file finished");
            return user;
        } catch (Exception e) {
            log.debug("Problems with file copying");
            throw new FileUploadException(String.format(env.getProperty(EXCEPTION_FILE_UPLOAD), file.getOriginalFilename()));
        }
    }

}