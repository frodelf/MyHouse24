package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.model.AdminForViewDTO;
import com.avada.myHouse24.repo.AdminRepository;
import com.avada.myHouse24.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
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

    @Override
    public Page<Admin> getPage(int pageNumber, Model model) {
        double size = 10.0;
        int max = (int)Math.ceil(adminRepository.findAll().size()/size-1);
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return adminRepository.findAll(pageRequest);
    }

    @Override
    public List<String> getOnlyName() {
        List<Admin> admins = adminRepository.findAll();
        List<String> names = new ArrayList<>();
        for (Admin admin : admins) {
            names.add(admin.getFirstName());
        }
        return names;
    }

    public Page<AdminForViewDTO> getPage(int pageNumber, Model model, List<AdminForViewDTO> adminList) {
        double size = 10.0;
        int max = (int) Math.ceil(adminList.size() / size-1 ) > 0 ? (int) Math.ceil(adminList.size() / size-1 ) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, adminList.size());
        List<AdminForViewDTO> pageList = adminList.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<AdminForViewDTO> userPage = new PageImpl<>(pageList, pageable, adminList.size());
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return userPage;
    }
}
