package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.UnitOfMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitOfMeasurementRepository extends JpaRepository<UnitOfMeasurement, Long> {
    Optional<UnitOfMeasurement> findByName(String name);
}
