package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.TemplateForInvoice;
import com.avada.myHouse24.repo.TemplateForInvoiceRepository;
import com.avada.myHouse24.services.TemplateForInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateForInvoiceServiceImpl implements TemplateForInvoiceService {
    private final TemplateForInvoiceRepository templateForInvoiceRepository;
    public void save(TemplateForInvoice templateForInvoice){
        templateForInvoiceRepository.save(templateForInvoice);
    }
    public TemplateForInvoice getById(Long id){
        return templateForInvoiceRepository.findById(id).get();
    }
    public void deleteById(Long id){
        templateForInvoiceRepository.deleteById(id);
    }
    public List<TemplateForInvoice> getAll(){
        return templateForInvoiceRepository.findAll();
    }
    public TemplateForInvoice getTemplateWhereIsMainIsTrue(){
        return templateForInvoiceRepository.findByIsMainIsTrue();
    }
}
