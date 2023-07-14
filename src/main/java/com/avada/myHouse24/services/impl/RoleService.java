package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Permission;
import com.avada.myHouse24.entity.Role;
import com.avada.myHouse24.repo.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void updateRolePermissions(long roleId, List<Permission> permissions) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        role.setPermissions(permissions);
        roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
