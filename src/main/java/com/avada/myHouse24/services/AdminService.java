package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Admin;

import java.util.List;

public interface AdminService {
    Admin getByName(String name);
    Admin getById(Long id);
    List<Admin> getAll();
    void save(Admin admin);

}
