package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    boolean existsByNumber(String number);
    Optional<Score> findByNumber(String number);
    List<Score> findAllByStatus(String status);
    List<Score> findAllByFlatIsNull();
    Page<Score> findByNumberContainingIgnoreCase(String search, Pageable pageable);
    @Query(value = "SELECT SUM(a.balance) FROM Score a")
    Double findAllBalance();
}