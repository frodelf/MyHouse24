package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.repo.UserRepository;
import com.avada.myHouse24.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public List<String> getOnlyName() {
        List<User> users = userRepository.findAll();
        List<String> names = new ArrayList<>();
        for (User user : users) {
            names.add(user.getFirstName());
        }
        return names;
    }

    public Page<User> getPage(int pageNumber, Model model) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int)Math.ceil(userRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(userRepository.findAll().size()/size-1) : 0;
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return userRepository.findAll(pageRequest);
    }

    public Page<UserForViewDTO> getPage(int pageNumber, Model model, List<UserForViewDTO> userList) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int) Math.ceil(userList.size() / size-1) > 0 ? (int) Math.ceil(userList.size() / size-1) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, userList.size());
        List<UserForViewDTO> pageList = userList.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<UserForViewDTO> userPage = new PageImpl<>(pageList, pageable, userList.size());
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return userPage;
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
        try {
            return userRepository.findByEmail(email).get();
        }catch (NoSuchElementException e){
            return null;
        }
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        log.info("IN verifyPassword password: {}", passwordEncoder.matches(password, user.getPassword()));
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void save(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    @Override
    public long getMaxId() {
        try {
            return userRepository.findMaxId() + 1;
        }catch (Exception e){
            return 1;
        }
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }


}
