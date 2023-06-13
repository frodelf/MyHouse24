package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.User;

import java.util.List;

public interface UserService {

    User register(User user);
    User getByFirstName(String name);
    List<User> getAll();

    User findUserByEmail(String email);
    boolean verifyPassword(User user, String password);
}
