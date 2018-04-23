package com.meetup.meetup.service;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.mail.MailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:links.properties")
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailBuilder mailBuilder;
    @Autowired
    private Environment environment;

    @Async
    public void sendMailRegistration(User user) throws MailException {
        MimeMessagePreparator messagePreparator = mailBuilder.setTo(user.getEmail())
                .setSubject("Meetup successful registration")
                .setVariable("name", user.getName() + ' ' + user.getLastname())
                .setVariable("login", user.getLogin())
                .setVariable("link", environment.getProperty("mail.login"))
                .setTemplate(MailBuilder.REGISTER_MAIL_TEMPLATE)
                .build();
        mailSender.send(messagePreparator);
    }

    @Async
    public void sendMailRecoveryPassword(User user, String token) throws MailException {
        MimeMessagePreparator messagePreparator = mailBuilder.setTo(user.getEmail())
                .setSubject("Password recovery")
                .setVariable("name", user.getName())
                .setVariable("link", environment.getProperty("mail.recovery") + token)
                .setTemplate(MailBuilder.RECOVERY_PASSWORD_TEMPLATE)
                .build();
        mailSender.send(messagePreparator);
    }
}