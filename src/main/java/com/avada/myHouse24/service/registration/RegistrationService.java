package com.avada.myHouse24.service.registration;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.entity.VerificationToken;
import com.avada.myHouse24.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class RegistrationService {

    @Autowired private UserRepository userRepository;
    @Autowired private VerificationTokenService verificationTokenService;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private JavaMailSender mailSender;
    @Autowired private EmailService emailService;

    @Autowired private ThreadPoolExecutor executor;

    public void register(RegistrationRequest request) {

        log.info(passwordEncoder.toString());

        User user = new User(request);
        String encoded_pass = passwordEncoder.encode(user.getPassword());
        log.info(encoded_pass);
        user.setPassword(encoded_pass);
        User savedUser = userRepository.save(user);
        log.info("In RegisterService.register(): {} saved successful" , savedUser);
        VerificationToken savedToken = verificationTokenService.createToken(savedUser);

        String html_template =
                "<a href='http://localhost:8080/login/confirm?token="
                        +savedToken.getToken()+
                        "'>Press to confirm registration!</a>";

        executor.execute(() -> emailService.send(savedUser.getEmail(), html_template));

    }

    @Transactional
    public boolean confirm (String token) {
        if(verificationTokenService.tokenExists(token)) {
            VerificationToken foundToken = verificationTokenService.getToken(token);
            if(foundToken != null) {
                if(foundToken.isValid()) {
                    log.info("Token valid");
                    verificationTokenService.enableUser(foundToken);
                    return true;
                } else {
                    log.info("Token expired!");
                    verificationTokenService.deleteToken(foundToken);
                    return false;
                }
            } else {
                log.info("Token not found in DB for some reason");
                return false;
            }
        } else {
            log.info("Token doesn't exist in DB!");
            return false;
        }
    }

}