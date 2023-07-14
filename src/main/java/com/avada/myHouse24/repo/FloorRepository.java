package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FloorRepository extends JpaRepository<Floor, Long> {
    @Query(value = "SELECT MAX(id) FROM Floor")
    Long findMaxId();
}
