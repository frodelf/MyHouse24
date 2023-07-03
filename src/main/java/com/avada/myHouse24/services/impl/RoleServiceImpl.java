package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Role;
import com.avada.myHouse24.repo.RoleRepository;
import com.avada.myHouse24.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role getById(long id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Role getByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public String getRoleName(String role) {
        String result = "";
        switch (role){
            case ("ROLE_ADMIN"):
                result = "Адмін";
                break;
            case ("ROLE_USER"):
                result = "Користувач";
                break;
        }
        return result;
    }
}
