package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByName(String name);
}