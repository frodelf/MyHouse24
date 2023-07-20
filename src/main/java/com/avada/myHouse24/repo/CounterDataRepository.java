package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.CounterData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterDataRepository extends JpaRepository<CounterData, Long> {

}
