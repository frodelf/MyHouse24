package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {
    Optional<House> findByName(String name);
    Page<House> findByNameContainingIgnoreCase(String search, Pageable pageable);
    long count();
}
