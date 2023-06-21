package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.repo.AdminRepository;
import com.avada.myHouse24.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    @Override
    public Admin getByName(String name) {
        return adminRepository.findByFirstName(name).get();
    }

    @Override
    public Admin getById(Long id) {
        return adminRepository.findById(id).get();
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public void save(Admin admin) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        admin.setPassword(encoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }
}
