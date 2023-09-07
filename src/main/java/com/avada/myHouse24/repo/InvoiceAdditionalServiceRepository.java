package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.InvoiceAdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceAdditionalServiceRepository extends JpaRepository<InvoiceAdditionalService, Long> {
    boolean existsByAdditionalService_Id(Long id);
}
