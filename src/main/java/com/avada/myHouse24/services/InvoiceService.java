package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Invoice;
import com.avada.myHouse24.model.InvoiceDto;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.sql.Date;
import java.util.List;

public interface InvoiceService {
    List<Invoice> getAll();
    Invoice getById(long id);
    void deleteById(long id);
    void save(Invoice invoice);
    Page<Invoice> getPage(int pageNumber, Model model);
    Page<InvoiceDto> getPage(int pageNumber, Model model, List<InvoiceDto> InvoiceDto);
    List<Invoice> forSelect(int page, int pageSize, String search);
    List<InvoiceDto> filter(InvoiceDto filter, String flatNumber, Date dateExample);
}
