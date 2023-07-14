package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Permission;
import com.avada.myHouse24.entity.Role;
import com.avada.myHouse24.model.RoleForm;
import com.avada.myHouse24.service.roles.PermissionService;
import com.avada.myHouse24.service.roles.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
@RequestMapping("/admin")
@Controller
public class RoleController {
    private final RoleService roleService;
    private final PermissionService permissionService;

    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }
    @PostMapping("/role-form")
    public String grantAccess(@ModelAttribute("roleForm") RoleForm roleForm) {
        List<Role> roles = roleForm.getRoles();

        for (Role role : roles) {
            Long roleId = role.getId();
            List<Permission> permissions = role.getPermissions();
            roleService.updateRolePermissions(roleId, permissions);
        }

        return "redirect:/admin/role-form";
    }
    @GetMapping("/role-form")
    public String showRoleForm(Model model) {
        List<Permission> permissions = permissionService.getAllPermissions();
        List<Role> roles = roleService.getAllRoles();
        RoleForm roleForm = new RoleForm();
        roleForm.setRoles(roles);
        model.addAttribute("roleForm", roleForm);
        model.addAttribute("permissions", permissions);

        return "/settings/roles";
    }
    @GetMapping("/reset-form")
    public String resetForm(Model model) {
        List<Permission> permissions = permissionService.getAllPermissions();
        List<Role> roles = roleService.getAllRoles();
        RoleForm roleForm = new RoleForm();
        roleForm.setRoles(roles);
        model.addAttribute("roleForm", roleForm);
        model.addAttribute("permissions", permissions);
        return "/settings/roles :: #roleForm";
    }

}