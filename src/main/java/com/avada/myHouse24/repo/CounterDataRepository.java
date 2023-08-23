package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.CounterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CounterDataRepository extends JpaRepository<CounterData, Long> {
    boolean existsByNumber(String number);
    @Query(value = "SELECT MAX(id) FROM CounterData")
    Long findMaxId();
}
