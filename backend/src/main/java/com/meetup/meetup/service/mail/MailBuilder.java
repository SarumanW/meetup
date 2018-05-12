package com.meetup.meetup.service.mail;

import com.meetup.meetup.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class MailBuilder {

    private static Logger log = LoggerFactory.getLogger(MailBuilder.class);

    private TemplateEngine templateEngine;

    private Context context;
    private String content;
    private String to;
    private String subject = "Meetup";
    private String fileName = null;
    private MultipartFile multipartFile;
    private File file;

    public MailBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.context = new Context();
    }

    public MailBuilder setTo(String email) {
        to = email;
        return this;
    }

    public MailBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public MailBuilder setTemplate(String template) {
        content = templateEngine.process(template, context);
        return this;
    }

    public MailBuilder setVariable(String name, Object value) {
        context.setVariable(name, value);
        return this;
    }

    public MailBuilder setFile(MultipartFile file) {
        this.multipartFile = file;
        System.out.println("setting "+file.getSize());
        this.fileName = "myFile.pdf";
        return this;
    }

    public MailBuilder setFile(File file, String date) {
        this.file = file;
        this.fileName = "events "+date+".pdf";
        return this;
    }

    public MimeMessagePreparator build() {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            if (multipartFile != null) {
                messageHelper.addAttachment(fileName, () -> multipartFile.getInputStream());
            }
            if (file != null){
                messageHelper.addAttachment(fileName,file);
            }
        };
    }
}

