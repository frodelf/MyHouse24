package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Role;
import com.avada.myHouse24.repo.RoleRepository;
import com.avada.myHouse24.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role getById(long id) {
        return roleRepository.findById(id).get();
    }
}
