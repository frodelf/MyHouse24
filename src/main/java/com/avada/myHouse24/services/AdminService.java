package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Admin;

import java.util.List;

public interface AdminService {
    Admin getByName(String name);
    List<Admin> getAll();
}
