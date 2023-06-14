package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.repo.UserRepository;
import com.avada.myHouse24.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public User getById(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN findAllUsers - {} users found", result.size());
        return result;
    }

    @Override
    public boolean isDebt(User user) {
        ArrayList<Flat> flats = new ArrayList<>();
        ArrayList<Score> scores = new ArrayList<>();
        for (Flat flat : flats) {
            scores.add(flat.getScore());
        }
        for (Score score : scores) {
            if(score.getBalance() < 0){
                return true;
            }
        }
        return false;
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

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    @Override
    public long getMaxId() {
        return userRepository.findMaxId()+1;
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
