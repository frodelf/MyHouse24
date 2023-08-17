package com.avada.myHouse24.repo;

import com.avada.myHouse24.entity.TemplateForInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateForInvoiceRepository extends JpaRepository<TemplateForInvoice, Long> {
    TemplateForInvoice findByIsMainIsTrue();
}
