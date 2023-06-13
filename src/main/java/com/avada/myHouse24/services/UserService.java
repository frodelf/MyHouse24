package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.User;

import java.util.List;

public interface UserService {
    User getByName(String name);
    List<User> getAll();
}
