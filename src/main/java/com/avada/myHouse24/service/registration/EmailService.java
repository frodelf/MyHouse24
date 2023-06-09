package com.avada.myHouse24.service.registration;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Service
@Log
public class EmailService {

    @Autowired
    Environment env;

    @Value("")
    private String EMAIL_SENDER;

    @Autowired
    private JavaMailSender mailSender;
    public void send(String to, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setText(content, true);
            helper.setTo(to);
            helper.setSubject("Email confirmation");

            log.info(EMAIL_SENDER);
            log.info(env.getProperty("spring.mail.username"));

            mailSender.send(message);

        } catch (MessagingException e) {
            log.info("fail to send email, msg: ");
            log.info(Arrays.toString(e.getStackTrace()));
        }
    }
}