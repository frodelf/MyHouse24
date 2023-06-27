package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Role;

import java.util.List;

public interface RoleService {
    Role getById(long id);
    Role getByName(String name);

    List<Role> getAll();
}
