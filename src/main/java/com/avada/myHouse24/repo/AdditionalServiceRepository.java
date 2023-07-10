package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, Long> {
    Optional<AdditionalService> findByName(String name);
}
