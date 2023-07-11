package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    boolean existsByNumber(String number);
    Optional<Score> findByNumber(String number);
}