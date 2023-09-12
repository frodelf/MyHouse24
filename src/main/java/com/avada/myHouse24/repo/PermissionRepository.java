package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Boolean existsByPage(String page);
}
