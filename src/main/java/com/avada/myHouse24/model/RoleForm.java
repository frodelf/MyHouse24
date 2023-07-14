package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.Permission;
import com.avada.myHouse24.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class RoleForm {
    private List<Role> roles;
}
