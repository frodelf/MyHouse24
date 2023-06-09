package com.avada.myHouse24.service.registration;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.entity.VerificationToken;
import com.avada.myHouse24.repo.UserRepository;
import com.avada.myHouse24.repo.VerificationTokenRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Log
public class VerificationTokenService {

    @Autowired private VerificationTokenRepository tokenRepository;
    @Autowired private UserRepository userRepository;

    public VerificationToken createToken(User user) {
        String tokenUUID = UUID.randomUUID().toString();
        VerificationToken token = new VerificationToken(tokenUUID,user);
        return tokenRepository.save(token);
    }

    public boolean tokenExists(String token) {
        return tokenRepository.existsByToken(token);
    }

    public VerificationToken getToken(String token) {
        return tokenRepository.findByToken(token).orElse(null);
    }

    public void deleteToken(VerificationToken token) {
        tokenRepository.delete(token);
    }

    @Transactional
    public void enableUser(VerificationToken token) {
        User tokenUser = token.getUser();
        tokenUser.setEnabled(true);
        userRepository.save(tokenUser);
        log.info(tokenUser.toString());
        deleteToken(token);
    }
}