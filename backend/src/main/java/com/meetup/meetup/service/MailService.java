package com.meetup.meetup.service;

import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.mail.MailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;

import java.io.File;

@Service
@PropertySource("classpath:links.properties")
public class MailService {

    private static Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String HTTP = "http://";

    //DOMAIN

    private static final String SERVER_DOMAIN = "server.domain";

    //PATHS

    private static final String CONFIRM_REGISTRATION_PATH = "mail.confirmRegistration";
    private static final String RECOVERY_PATH = "mail.recovery";
    private static final String LOGIN_PATH = "mail.login";

    //TEMPLATES

    private static final String CONFIRM_REGISTRATION_TEMPLATE = "confirmationRegistrationTemplate";
    private static final String EVENT_PLAN_TEMPLATE = "eventPlanTemplate";
    private static final String RECOVERY_PASSWORD_TEMPLATE = "recoveryPasswordTemplate";
    private static final String REGISTER_MAIL_TEMPLATE = "registerMailTemplate";

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
    public void sendMailConfirmationRegistration(User user, String token) {
        log.debug("Trying to build message");

        MimeMessagePreparator messagePreparator = new MailBuilder(templateEngine)
                .setTo(user.getEmail())
                .setSubject("Confirmation of registration")
                .setVariable("name", user.getName())
                .setVariable("link", HTTP +
                        environment.getProperty(SERVER_DOMAIN) +
                        environment.getProperty(CONFIRM_REGISTRATION_PATH) + token)
                .setTemplate(environment.getProperty(CONFIRM_REGISTRATION_TEMPLATE))
                .build();

        log.debug("Trying to send message");

        mailSender.send(messagePreparator);

        log.debug("Mail was sent successfully");
    }

    @Async
    public void sendMailSuccessfulRegistration(User user) {
        log.debug("Trying to build message");

        MimeMessagePreparator messagePreparator = new MailBuilder(templateEngine)
                .setTo(user.getEmail())
                .setSubject("Meetup successful registration")
                .setVariable("name", user.getName() + ' ' + user.getLastname())
                .setVariable("login", user.getLogin())
                .setVariable("link", HTTP +
                        environment.getProperty(SERVER_DOMAIN) +
                        environment.getProperty(LOGIN_PATH))
                .setTemplate(environment.getProperty(REGISTER_MAIL_TEMPLATE))
                .build();

        log.debug("Trying to send message");

        mailSender.send(messagePreparator);

        log.debug("Mail was sent successfully");
    }

    @Async
    public void sendMailRecoveryPassword(User user, String token) {
        log.debug("Trying to build message");

        MimeMessagePreparator messagePreparator = new MailBuilder(templateEngine)
                .setTo(user.getEmail())
                .setSubject("Password recovery")
                .setVariable("name", user.getName())
                .setVariable("link", HTTP +
                        environment.getProperty(SERVER_DOMAIN) +
                        environment.getProperty(RECOVERY_PATH) + token)
                .setTemplate(environment.getProperty(RECOVERY_PASSWORD_TEMPLATE))
                .build();

        log.debug("Trying to send message");

        mailSender.send(messagePreparator);

        log.debug("Mail was sent successfully");
    }

    @Async
    public void sendMailWithEventPlan(User user, MultipartFile file) {
        log.debug("Trying to build message");

        log.debug("Mail service : " + file.getSize());

        MimeMessagePreparator messagePreparator = new MailBuilder(templateEngine)
                .setTo(user.getEmail())
                .setSubject("Event Plan")
                .setVariable("name", user.getName())
                .setTemplate(environment.getProperty(EVENT_PLAN_TEMPLATE))
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
                .setTemplate(environment.getProperty(EVENT_PLAN_TEMPLATE))
                .setFile(file, date)
                .build();

        log.debug("Trying to send message");

        mailSender.send(messagePreparator);

        log.debug("Mail was sent successfully");
    }
}
