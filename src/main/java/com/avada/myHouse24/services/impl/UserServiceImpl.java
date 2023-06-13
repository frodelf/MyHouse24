package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.repository.UserRepository;
import com.avada.myHouse24.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User getByName(String name) {
        return userRepository.findByName(name).get();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
