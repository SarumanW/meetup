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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;

@Component
public class JwtService {

    private static Logger log = LoggerFactory.getLogger(JwtService.class);

    private static final String SUBJECT = "meetup";

    private static final String ISSUER = "com.meetup";

    private static final String LOGIN = "login";
    private static final String EMAIL = "email";

    private final SecretKeyProvider secretKeyProvider;
    private final UserDao userDao;

    @Autowired
    public JwtService(SecretKeyProvider secretKeyProvider, UserDao userDao) {
        this.secretKeyProvider = secretKeyProvider;
        this.userDao = userDao;
    }

    public User verify(String token) throws Exception {
        log.debug("Trying to get secret key form SecretKeyProvider");

        byte[] secretKey = secretKeyProvider.getKey();

        log.debug("Trying to parse email from token '{}'", token);

        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        String login = claims.getBody().get(LOGIN).toString();

        log.debug("Login '{}' was parsed successfully", login);

        return userDao.findByLogin(login);
    }

    public User verifyForRecoveryPassword(String token) throws Exception {
        log.debug("Trying to get secret key form SecretKeyProvider");

        byte[] secretKey = secretKeyProvider.getKey();

        log.debug("Trying to parse email from token '{}'", token);

        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        String email = claims.getBody().get(EMAIL).toString();

        log.debug("Email '{}' was parsed successfully", email);

        return userDao.findByEmail(email);
    }

    public String tokenFor(User user) throws Exception {
        log.debug("Trying to get secret key form SecretKeyProvider");

        byte[] secretKey = secretKeyProvider.getKey();

        log.debug("Trying to build a token for user '{}'", user);

        Date expiration = Date.from(LocalDateTime.now(UTC).plusDays(365).toInstant(UTC));
        return Jwts.builder()
                .setSubject(SUBJECT)
                .setExpiration(expiration)
                .setIssuer(ISSUER)
                .claim(LOGIN, user.getLogin())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String tokenForRecoveryPassword(User user) throws Exception {
        log.debug("Trying to get secret key form SecretKeyProvider");

        byte[] secretKey = secretKeyProvider.getKey();

        log.debug("Trying to build a token for user '{}'", user);

        Date expiration = Date.from(LocalDateTime.now(UTC).plusMinutes(5).toInstant(UTC));
        return Jwts.builder()
                .setSubject(SUBJECT)
                .setExpiration(expiration)
                .setIssuer(ISSUER)
                .claim(EMAIL, user.getEmail())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
