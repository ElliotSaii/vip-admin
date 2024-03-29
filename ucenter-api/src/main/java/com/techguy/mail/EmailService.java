package com.techguy.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendMail(Email email) throws MessagingException {
        String from ="M-Cash <viporgcenter@gmail.com>";
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(email.getProperties());

        helper.setFrom(new InternetAddress(from));

        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        String html = springTemplateEngine.process(email.getTemplate(), context);
        helper.setText(html,true);
        emailSender.send(message);

        log.info("Send mail to=> {} with code= {}",email.getTo(),email.getProperties().get("code"));
    }
}
