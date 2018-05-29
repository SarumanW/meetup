package com.meetup.meetup.security.jwt;

import com.meetup.meetup.exception.runtime.SecretKeyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.meetup.meetup.keys.Key.EXCEPTION_KEY_NOT_FOUND;

@Component
@PropertySource("classpath:strings.properties")
public class SecretKeyProvider {

    private static Logger log = LoggerFactory.getLogger(SecretKeyProvider.class);

    @Autowired
    private Environment env;

    public byte[] getKey() {
        log.debug("Trying to get secret key");
        try {

            byte[] secretKey = Files.readAllBytes(Paths.get(this.getClass().getResource("/jwt.key").toURI()));

            log.debug("Found secret key");

            return secretKey;
        } catch (URISyntaxException | IOException e) {
            log.error("Secret key is not available", e);
            throw new SecretKeyNotFoundException(env.getProperty(EXCEPTION_KEY_NOT_FOUND));
        }
    }
}
