package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.enums.Theme;
import com.avada.myHouse24.model.AdminForViewDTO;
import com.avada.myHouse24.model.UserForViewDTO;
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
import java.util.stream.Collectors;

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
        adminRepository.save(admin);
    }

    @Override
    public Page<Admin> getPage(int pageNumber, Model model) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int)Math.ceil(adminRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(adminRepository.findAll().size()/size-1) : 0;
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
        pageNumber = pageNumber - 1;
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
    public List<AdminForViewDTO> filter(AdminForViewDTO adminForViewDto, List<AdminForViewDTO> admins){
        if(!adminForViewDto.getFullName().isBlank()){
            admins = admins.stream()
                    .filter(dto -> dto.getFullName() != null && dto.getFullName().contains(adminForViewDto.getFullName()))
                    .collect(Collectors.toList());
        }
        if(!adminForViewDto.getRole().isBlank()){
            admins = admins.stream()
                    .filter(dto -> dto.getRole() != null && dto.getRole().contains(adminForViewDto.getRole()))
                    .collect(Collectors.toList());
        }
        if(!adminForViewDto.getPhone().isBlank()){
            admins = admins.stream()
                    .filter(dto -> dto.getPhone() != null && dto.getPhone().contains(adminForViewDto.getPhone()))
                    .collect(Collectors.toList());
        }
        if(!adminForViewDto.getEmail().isBlank()){
            admins = admins.stream()
                    .filter(dto -> dto.getEmail() != null && dto.getEmail().contains(adminForViewDto.getEmail()))
                    .collect(Collectors.toList());
        }
        if (adminForViewDto.getStatus() != null) {
            admins = admins.stream()
                    .filter(dto -> dto.getStatus() != null && dto.getStatus() == adminForViewDto.getStatus())
                    .collect(Collectors.toList());
        }
        return admins;
    }
    public Admin getAuthAdmin(){
        Admin admin = new Admin();
        admin.setTheme(Theme.DARK);
        return admin;
    }
    public Boolean existByEmail(String email){
        return adminRepository.existsByEmail(email);
    }
}
