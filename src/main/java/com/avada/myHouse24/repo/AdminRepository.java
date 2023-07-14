package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Admin;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.function.Function;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);
}
