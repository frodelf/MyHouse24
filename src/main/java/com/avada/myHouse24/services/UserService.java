package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.User;

import java.util.List;

public interface UserService {

    User register(User user);
    User getByFirstName(String name);
    User getById(long id);
    List<User> getAll();
    boolean isDebt(User user);
    User findUserByEmail(String email);
    boolean verifyPassword(User user, String password);
    void save(User user);
    boolean existsById(long id);
    long getMaxId();
    void deleteById(long id);
}
