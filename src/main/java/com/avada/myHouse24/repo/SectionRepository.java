package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findByName(String name);
    @Query(value = "SELECT MAX(id) FROM Section")
    Long findMaxId();
}
