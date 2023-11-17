package com.avada.myHouse24.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Autowired
    Environment environment;

    @Bean("javaMailSender")
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setDefaultEncoding("UTF-8");
        sender.setHost(environment.getProperty("spring.mail.host"));
        sender.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.mail.port"))));
        sender.setUsername(environment.getProperty("spring.mail.username"));
        sender.setPassword(environment.getProperty("spring.mail.password"));

        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return sender;
    }

    @Bean
    public ThreadPoolExecutor executor() {
        return new ThreadPoolExecutor(10, 10, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }
}