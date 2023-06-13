package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.repo.UserRepository;
import com.avada.myHouse24.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered", registeredUser);
        return registeredUser;
    }

    @Override
    public User getByFirstName(String name) {
        return userRepository.findByFirstName(name).get();
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN findAllUsers - {} users found", result.size());
        return result;
    }

    @Override
    public User findUserByEmail(String email) {
        User result = userRepository.findByEmail(email).get();
        log.info("IN findUserByEmail - {} successfully found", result);
        return result;
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        log.info("IN verifyPassword password: {}", passwordEncoder.matches(password, user.getPassword()));
        return passwordEncoder.matches(password, user.getPassword());
    }
}
