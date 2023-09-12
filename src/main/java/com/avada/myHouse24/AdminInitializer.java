package com.avada.myHouse24;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.entity.Permission;
import com.avada.myHouse24.entity.Role;
import com.avada.myHouse24.enums.Permissions;
import com.avada.myHouse24.enums.Roles;
import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.repo.PermissionRepository;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.PermissionService;
import com.avada.myHouse24.services.impl.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final AdminServiceImpl adminService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        Permissions[] permissions = Permissions.values();
        for (Permissions permission : permissions) {
            if(!permissionService.existByPage(permission.name())){
                Permission permission1 = new Permission();
                permission1.setPage(permission.name());
                permissionRepository.save(permission1);
            }
        }

        Roles[] roles = Roles.values();
        for (Roles role : roles) {
            if(!roleService.existByName(role.name())){
                Role role1 = new Role();
                role1.setName(role.name());
                role1.setPermissions(permissionService.getAllPermissions());
                roleService.save(role1);
            }
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Admin admin = new Admin();
        admin.setPassword(bCryptPasswordEncoder.encode("admin"));
        admin.setEmail("admin@gmail.com");
        admin.setFirstName("admin");
        admin.setStatus(UserStatus.ACTIVE);
        admin.setRole(roleService.getById(2));
        if(!adminService.existByEmail(admin.getEmail())){
            adminService.save(admin);
        }
    }
}
