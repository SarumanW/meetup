package com.meetup.meetup.service.mail;

import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



public class MailBuilder {

    private TemplateEngine templateEngine;

    private Context context;
    private String content;
    private String to;
    private String subject = "Meetup";

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

    public MimeMessagePreparator build() {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };
    }
}
