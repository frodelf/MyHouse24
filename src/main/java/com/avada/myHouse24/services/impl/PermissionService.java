package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Permission;
import com.avada.myHouse24.repo.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
    public Boolean existByPage(String page){
        return permissionRepository.existsByPage(page);
    }
}
