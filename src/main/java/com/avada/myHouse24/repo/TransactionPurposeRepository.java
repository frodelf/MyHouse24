package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.TransactionPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionPurposeRepository extends JpaRepository<TransactionPurpose, Long> {
    Optional<TransactionPurpose> findByName(String name);
}
