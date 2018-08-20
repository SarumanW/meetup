package com.meetup.meetup.service;

import com.meetup.meetup.exception.runtime.frontend.detailed.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.meetup.meetup.keys.Key.EXCEPTION_FILE_UPLOAD;

@Service
@PropertySource("classpath:links.properties")
@PropertySource("classpath:strings.properties")
public class EventImageService {

    @Autowired
    private Environment env;

    public String store(MultipartFile file) {

        Path rootLocation = Paths.get(env.getProperty("event.local.img.link"));
        long currentTime = System.currentTimeMillis();
        String inFileFormat = "." + file.getOriginalFilename().split("\\.")[1];
        String filePath = "\"" + env.getProperty("event.remote.img.link") + currentTime + inFileFormat + "\"";

        try {
            Files.deleteIfExists(rootLocation.resolve(currentTime + inFileFormat));
            Files.copy(file.getInputStream(), rootLocation.resolve(currentTime + inFileFormat));
        } catch (Exception e) {
            throw new FileUploadException(String.format(env.getProperty(EXCEPTION_FILE_UPLOAD),file.getOriginalFilename()));
        }

        return filePath;
    }
}
