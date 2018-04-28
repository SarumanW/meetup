package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.jwt.SecretKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;


@PropertySource("classpath:image.properties")
@PropertySource("classpath:jwt.properties")
@Component
public class JwtService {

    @Autowired
    Environment env;




    private final SecretKeyProvider secretKeyProvider;
    private final UserDao userDao;

    @Autowired
    public JwtService(SecretKeyProvider secretKeyProvider, UserDao userDao) {
        this.secretKeyProvider = secretKeyProvider;
        this.userDao = userDao;
    }

    public User verify(String token) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return userDao.findByLogin(claims.getBody().get(env.getProperty("jwt.login")).toString());
    }

    public User verifyForRecoveryPassword(String token) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return userDao.findByEmail(claims.getBody().get(env.getProperty("jwt.email")).toString());
    }

    public String tokenFor(User user) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        Date expiration = Date.from(LocalDateTime.now(UTC).plusDays(365).toInstant(UTC));
        return Jwts.builder()
                .setSubject(env.getProperty("jwt.subject"))
                .setExpiration(expiration)
                .setIssuer(env.getProperty("jwt.issuer"))
                .claim(env.getProperty("jwt.login"), user.getLogin())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String tokenForRecoveryPassword(User user) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        Date expiration = Date.from(LocalDateTime.now(UTC).plusMinutes(5).toInstant(UTC));
        return Jwts.builder()
                .setSubject(env.getProperty("jwt.subject"))
                .setExpiration(expiration)
                .setIssuer(env.getProperty("jwt.issuer"))
                .claim(env.getProperty("jwt.email"), user.getEmail())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
