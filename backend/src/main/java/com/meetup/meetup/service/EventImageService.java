package com.meetup.meetup.service;

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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.meetup.meetup.Keys.Key.EXCEPTION_FILE_UPLOAD;

@Service
@PropertySource("classpath:links.properties")
@PropertySource("classpath:strings.properties")
public class EventImageService {

    @Autowired
    private Environment env;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());


    public String store(MultipartFile file) {
        log.debug("Try to receive img");
        Path rootLocation = Paths.get(env.getProperty("event.local.img.link"));
        log.debug("Try to receive img to path: '{}'", rootLocation.getFileName());
        long currentTime = System.currentTimeMillis();
        String inFileFormat = "." + file.getOriginalFilename().split("\\.")[1];
        log.debug("File format is {}", inFileFormat);
        String filePath = "\"" + env.getProperty("event.remote.img.link") + currentTime + inFileFormat + "\"";
        
        try {
            log.debug("Copying file");
            Files.deleteIfExists(rootLocation.resolve(currentTime + inFileFormat));
            Files.copy(file.getInputStream(), rootLocation.resolve(currentTime + inFileFormat));
            log.debug("Copying file finished");
        } catch (Exception e) {
            log.debug("Problems with file copying");
            throw new FileUploadException(String.format(env.getProperty(EXCEPTION_FILE_UPLOAD), file.getOriginalFilename()));
        }
        return filePath;
    }
}
