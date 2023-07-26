package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.Invoice;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findByNumberContainingIgnoreCase(String search, Pageable pageable);
}
