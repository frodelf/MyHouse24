package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
        List<Flat> flats = user.getFlats();
        if(flats != null)for (Flat flat : flats) {
            if(flat.getScore() != null  &&  flat.getScore().getBalance() < 0){
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

    public List<User> forSelect(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<User> userPage;

        if (search != null && !search.isEmpty()) {
            userPage = userRepository.findByFirstNameContainingIgnoreCase(search, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        return userPage.getContent();
    }
    public List<UserForViewDTO> filter(UserForViewDTO userForViewDTO, List<UserForViewDTO> users, Date date, String flat, String house){
        if (date.equals(new Date(2023, 01, 01))) userForViewDTO.setDate(date);
        if(date != null && date.getYear() != -900){
            userForViewDTO.setDate(date);
        }
        if (!userForViewDTO.getId().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getId() != null && dto.getId().contains(userForViewDTO.getId()))
                    .collect(Collectors.toList());
        }

        if (!userForViewDTO.getFullName().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getFullName() != null && dto.getFullName().contains(userForViewDTO.getFullName()))
                    .collect(Collectors.toList());
        }

        if (!userForViewDTO.getPhone().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getPhone() != null && dto.getPhone().contains(userForViewDTO.getPhone()))
                    .collect(Collectors.toList());
        }

        if (!userForViewDTO.getEmail().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getEmail() != null && dto.getEmail().contains(userForViewDTO.getEmail()))
                    .collect(Collectors.toList());
        }

        if (!house.isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getHouses().contains(house))
                    .collect(Collectors.toList());
        }

        if (!flat.isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getFlats().contains(flat))
                    .collect(Collectors.toList());
        }
        if (userForViewDTO.getDate() != null) {
            users = users.stream()
                    .filter(dto -> dto.getDate() != null && dto.getDate().equals(date))
                    .collect(Collectors.toList());
        }

        if (!userForViewDTO.getStatus().isBlank()) {
            users = users.stream()
                    .filter(dto -> dto.getStatus() != null && dto.getStatus().equals(userForViewDTO.getStatus()))
                    .collect(Collectors.toList());
        }

        if (userForViewDTO.getIsDebt() != null) {
            users = users.stream()
                    .filter(dto -> dto.getIsDebt() != null && dto.getIsDebt().equals(userForViewDTO.getIsDebt()))
                    .collect(Collectors.toList());
        }
        return users;
    }
}
