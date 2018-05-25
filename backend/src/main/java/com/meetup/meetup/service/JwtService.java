package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.jwt.SecretKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;


@PropertySource("classpath:image.properties")
@PropertySource("classpath:jwt.properties")
@Component
public class JwtService {

    private static Logger log = LoggerFactory.getLogger(JwtService.class);

    private final static String JWT_SUBJECT = "jwt.subject";
    private final static String JWT_ISSUER = "jwt.issuer";
    private final static String JWT_LOGIN = "jwt.login";
    private final static String JWT_EMAIL = "jwt.email";

    @Autowired
    private Environment env;

    private final SecretKeyProvider secretKeyProvider;
    private final UserDao userDao;

    @Autowired
    public JwtService(SecretKeyProvider secretKeyProvider, UserDao userDao) {
        this.secretKeyProvider = secretKeyProvider;
        this.userDao = userDao;
    }

    public User verify(String token) {
        log.debug("Trying to get secret key form SecretKeyProvider");

        byte[] secretKey = secretKeyProvider.getKey();

        log.debug("Trying to parse email from token '{}'", token);

        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        String login = claims.getBody().get(env.getProperty(JWT_LOGIN)).toString();

        log.debug("Login '{}' was parsed successfully", login);

        return userDao.findByLogin(login);
    }

    public User verifyForRecoveryPassword(String token) {
        log.debug("Trying to get secret key form SecretKeyProvider");

        byte[] secretKey = secretKeyProvider.getKey();

        log.debug("Trying to parse email from token '{}'", token);

        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        String email = claims.getBody().get(env.getProperty(JWT_EMAIL)).toString();

        log.debug("Email '{}' was parsed successfully", email);

        return userDao.findByEmail(email);
    }

    public String tokenFor(User user) {
        log.debug("Trying to get secret key form SecretKeyProvider");

        byte[] secretKey = secretKeyProvider.getKey();

        log.debug("Trying to build a token for user '{}'", user);

        Date expiration = Date.from(LocalDateTime.now(UTC).plusDays(365).toInstant(UTC));
        return Jwts.builder()
                .setSubject(env.getProperty(JWT_SUBJECT))
                .setExpiration(expiration)
                .setIssuer(env.getProperty(JWT_ISSUER))
                .claim(env.getProperty(JWT_LOGIN), user.getLogin())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String tokenForConfirmationRegistration(User user) {
        log.debug("Trying to get secret key form SecretKeyProvider");

        byte[] secretKey = secretKeyProvider.getKey();

        log.debug("Trying to build a token for user '{}'", user);

        Date expiration = Date.from(LocalDateTime.now(UTC).plusDays(1).toInstant(UTC));
        return Jwts.builder()
                .setSubject(env.getProperty(JWT_SUBJECT))
                .setExpiration(expiration)
                .setIssuer(env.getProperty(JWT_ISSUER))
                .claim(env.getProperty(JWT_EMAIL), user.getEmail())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String tokenForRecoveryPassword(User user) {
        log.debug("Trying to get secret key form SecretKeyProvider");

        byte[] secretKey = secretKeyProvider.getKey();

        log.debug("Trying to build a token for user '{}'", user);

        Date expiration = Date.from(LocalDateTime.now(UTC).plusMinutes(15).toInstant(UTC));
        return Jwts.builder()
                .setSubject(env.getProperty(JWT_SUBJECT))
                .setExpiration(expiration)
                .setIssuer(env.getProperty(JWT_ISSUER))
                .claim(env.getProperty(JWT_EMAIL), user.getEmail())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
