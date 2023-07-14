package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FlatRepository extends JpaRepository<Flat, Long> {
    @Query(value = "SELECT MAX(id) FROM Flat")
    Long findMaxId();
}
