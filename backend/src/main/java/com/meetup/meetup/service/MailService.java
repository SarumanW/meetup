package com.meetup.meetup.service;

import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Component
public class MailService {

    private static final String username = "nc.meetup4@gmail.com";
    private static final String password = "kd234cdEa@q4AeRl40";
    private Session session;
    public static final String templateRegister = "Hello, <b>%s! </b> " +
            "<br>Your nickname: %s" +
            "<br>Your password: %s" +
            "<br>Use this data to <a href=\"/login\">login</a> in your account";

    public MailService() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public void sendMail(String address, String subject, String mail) throws MessagingException {
            Message message = new MimeMessage(session);
            Multipart multipart = new MimeMultipart(" alternative");
            message.setFrom(new InternetAddress());
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(address));
            message.setSubject(subject);

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mail, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);
            message.saveChanges();

            Transport.send(message);

    }

}
