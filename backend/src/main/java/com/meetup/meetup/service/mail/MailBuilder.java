package com.meetup.meetup.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MailBuilder {
    public static final String REGISTER_MAIL_TEMPLATE = "registerMailTemplate";
    public static final String RECOVERY_PASSWORD_TEMPLATE = "recoveryPasswordTemplate";

    @Autowired
    private TemplateEngine templateEngine;

    private Context context;
    private String content;
    private String to;
    private String subject = "Meetup";

    @PostConstruct
    public void init() {
        context = new Context();
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
