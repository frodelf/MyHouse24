package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.TemplateForInvoice;

import java.util.List;

public interface TemplateForInvoiceService {
    void save(TemplateForInvoice templateForInvoice);
    TemplateForInvoice getById(Long id);
    void deleteById(Long id);
    List<TemplateForInvoice> getAll();
    TemplateForInvoice getTemplateWhereIsMainIsTrue();
}
