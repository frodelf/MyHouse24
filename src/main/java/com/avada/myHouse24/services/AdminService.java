package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.model.AdminForViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;

public interface AdminService {
    Admin getByName(String name);
    Admin getById(Long id);
    List<Admin> getAll();
    void save(Admin admin);
    Page<Admin> getPage(int pageNumber, Model model);
    List<String> getOnlyName();
    Page<AdminForViewDTO> getPage(int pageNumber, Model model, List<AdminForViewDTO> adminList);
    List<AdminForViewDTO> filter(AdminForViewDTO adminForViewDto, List<AdminForViewDTO> admins);
    Admin getAuthAdmin();
}
