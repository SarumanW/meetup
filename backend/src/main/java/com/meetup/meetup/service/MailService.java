package com.meetup.meetup.service;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.mail.MailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thymeleaf.TemplateEngine;

import java.io.File;
import java.io.InputStream;

@Service
@PropertySource("classpath:links.properties")
public class MailService {

    private static Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String HTTP = "http://";

    private final JavaMailSender mailSender;
    private final Environment environment;
    private final TemplateEngine templateEngine;

    @Autowired
    public MailService(JavaMailSender mailSender, Environment environment, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.environment = environment;
        this.templateEngine = templateEngine;
    }


    @Async
    public void sendMailRegistration(User user) throws MailException {
        log.debug("Trying to build message");

        MimeMessagePreparator messagePreparator = new MailBuilder(templateEngine)
                .setTo(user.getEmail())
                .setSubject("Meetup successful registration")
                .setVariable("name", user.getName() + ' ' + user.getLastname())
                .setVariable("login", user.getLogin())
                .setVariable("link", HTTP +
                        environment.getProperty("server.domain") +
                        environment.getProperty("mail.login"))
                .setTemplate(environment.getProperty("registerMailTemplate"))
                .build();
        log.debug("Trying to send message");

        mailSender.send(messagePreparator);

        log.debug("Mail was sent successfully");
    }

    @Async
    public void sendMailRecoveryPassword(User user, String token) throws MailException {
        log.debug("Trying to build message");

        MimeMessagePreparator messagePreparator = new MailBuilder(templateEngine)
                .setTo(user.getEmail())
                .setSubject("Password recovery")
                .setVariable("name", user.getName())
                .setVariable("link", HTTP +
                        environment.getProperty("server.domain") +
                        environment.getProperty("mail.recovery") + token)
                .setTemplate(environment.getProperty("recoveryPasswordTemplate"))
                .build();
        log.debug("Trying to send message");

        mailSender.send(messagePreparator);

        log.debug("Mail was sent successfully");
    }

    @Async
    public void sendMailWithEventPlan(User user, MultipartFile file) {
        log.debug("Trying to build message");
        System.out.println("Mail service : " + file.getSize());
        MimeMessagePreparator messagePreparator = new MailBuilder(templateEngine)
                .setTo(user.getEmail())
                .setSubject("Event Plan")
                .setVariable("name", user.getName())
                .setTemplate(environment.getProperty("eventPlanTemplate"))
                .setFile(file)
                .build();
        log.debug("Trying to send message");

        mailSender.send(messagePreparator);

        log.debug("Mail was sent successfully");
    }

    @Async
    public void sendMailWithEventPlan(User user, File file, String date) {
        log.debug("Trying to build message");
        MimeMessagePreparator messagePreparator = new MailBuilder(templateEngine)
                .setTo(user.getEmail())
                .setSubject("Event Plan")
                .setVariable("name", user.getName())
                .setTemplate(environment.getProperty("eventPlanTemplate"))
                .setFile(file, date)
                .build();
        log.debug("Trying to send message");

        mailSender.send(messagePreparator);

        log.debug("Mail was sent successfully");
    }
}
