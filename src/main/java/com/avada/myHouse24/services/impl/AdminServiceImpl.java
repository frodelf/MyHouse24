package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.repository.AdminRepository;
import com.avada.myHouse24.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    @Override
    public Admin getByName(String name) {
        return adminRepository.findByName(name).get();
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }
}
