package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Role;
import com.avada.myHouse24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
