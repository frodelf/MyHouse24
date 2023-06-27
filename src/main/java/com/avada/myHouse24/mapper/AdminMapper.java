package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.model.AdminForAddDto;
import com.avada.myHouse24.model.AdminForViewDto;
import com.avada.myHouse24.services.impl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminMapper {
    private final RoleServiceImpl roleService;
    public AdminForAddDto toDtoForAdd(Admin admin){
        AdminForAddDto adminForAddDto = new AdminForAddDto();
        adminForAddDto.setId(admin.getId());
        adminForAddDto.setFirstName(admin.getFirstName());
        adminForAddDto.setLastName(admin.getFirstName());
        adminForAddDto.setPhone(admin.getPhone());
        adminForAddDto.setRole(admin.getRole().getName());
        adminForAddDto.setStatus(admin.getStatus());
        adminForAddDto.setEmail(admin.getEmail());
        adminForAddDto.setPassword(admin.getPassword());
        return adminForAddDto;
    }

    public Admin toEntityForAdd(AdminForAddDto adminDto){
        Admin admin = new Admin();
        if(adminDto.getId() != null)admin.setId(adminDto.getId());
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setPhone(adminDto.getPhone());
        admin.setRole(roleService.getByName(adminDto.getRole()));
        admin.setStatus(adminDto.getStatus());
        admin.setEmail(adminDto.getEmail());
        admin.setPassword(adminDto.getPassword());
        return admin;
    }

    public AdminForViewDto toDtoForView(Admin admin){
        AdminForViewDto adminForViewDto = new AdminForViewDto();
        adminForViewDto.setId(String.valueOf(admin.getId()));
        adminForViewDto.setFullName(admin.getFirstName() == null ? "" : admin.getFirstName() + " "+admin.getLastName() == null ? "" : admin.getLastName());
        if(admin.getRole() != null)adminForViewDto.setRole(admin.getRole().getName());
        adminForViewDto.setPhone(admin.getPhone());
        adminForViewDto.setEmail(admin.getEmail());
        adminForViewDto.setStatus(admin.getStatus());
        return adminForViewDto;
    }

    public List<AdminForViewDto> toDtoListForView(List<Admin> admins){
        List<AdminForViewDto> adminForViewDtoList = new ArrayList<>();
        for (Admin admin : admins) {
            adminForViewDtoList.add(toDtoForView(admin));
        }
        return adminForViewDtoList;
    }
}
