package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Invoice;
import com.avada.myHouse24.mapper.InvoiceMapper;
import com.avada.myHouse24.model.HouseForViewDto;
import com.avada.myHouse24.model.InvoiceDto;
import com.avada.myHouse24.repo.InvoiceRepository;
import com.avada.myHouse24.services.InvoiceService;
import com.avada.myHouse24.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.sql.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    @Override
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice getById(long id) {
        return invoiceRepository.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        invoiceRepository.deleteById(id);
    }

    @Override
    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }
    @Override
    public Page<Invoice> getPage(int pageNumber, Model model) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int)Math.ceil(invoiceRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(invoiceRepository.findAll().size()/size-1) : 0;
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return invoiceRepository.findAll(pageRequest);
    }

    @Override
    public Page<InvoiceDto> getPage(int pageNumber, Model model, List<InvoiceDto> InvoiceDtoS) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int) Math.ceil(InvoiceDtoS.size() / size-1 ) > 0 ? (int) Math.ceil(InvoiceDtoS.size() / size-1 ) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, InvoiceDtoS.size());
        List<InvoiceDto> pageList = InvoiceDtoS.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<InvoiceDto> invoicePage = new PageImpl<>(pageList, pageable, InvoiceDtoS.size());
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return invoicePage;
    }
    @Override
    public List<Invoice> forSelect(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Invoice> invoices;

        if (search != null && !search.isEmpty()) {
            invoices = invoiceRepository.findByNumberContainingIgnoreCase(search, pageable);
        } else {
            invoices = invoiceRepository.findAll(pageable);
        }

        return invoices.getContent();
    }
    public List<InvoiceDto> filter(InvoiceDto filter, String flatNumber, Date dateExample){
        List<InvoiceDto> invoiceDtos = invoiceMapper.toDtoList(invoiceRepository.findAll());
        if(filter.getNumber() != null){
            invoiceDtos = invoiceDtos.stream()
                    .filter(dto -> dto.getNumber() != null && dto.getNumber().contains(filter.getNumber()))
                    .collect(Collectors.toList());
        }
        if(!filter.getStatus().isBlank()){
            invoiceDtos = invoiceDtos.stream()
                    .filter(dto -> dto.getStatus() != null && dto.getStatus().equals(filter.getStatus()))
                    .collect(Collectors.toList());
        }
        if(!filter.getMonths().isBlank()){
            invoiceDtos = invoiceDtos.stream()
                    .filter(dto -> dto.getMonths() != null && dto.getMonths().equals(DateUtil.toMonthForMY(filter.getMonths(), new Locale("uk"))))
                    .collect(Collectors.toList());
        }
        if(dateExample != null && !Objects.equals(dateExample.toString(), "1000-01-01")){
            invoiceDtos = invoiceDtos.stream()
                    .filter(dto -> dto.getDate() != null && dto.getDate().toString().contains(dateExample.toString()))
                    .collect(Collectors.toList());
        }
        if(!flatNumber.isBlank()){
            invoiceDtos = invoiceDtos.stream()
                    .filter(dto -> dto.getFlat() != null && String.valueOf(dto.getFlat().getNumber()).contains(flatNumber))
                    .collect(Collectors.toList());
        }
        if(filter.getUser() != null){
            invoiceDtos = invoiceDtos.stream()
                    .filter(dto -> dto.getUser() != null && Objects.equals(dto.getUser().getId(), filter.getUser().getId()))
                    .collect(Collectors.toList());
        }
        if(filter.getAddToStats() != null){
            invoiceDtos = invoiceDtos.stream()
                    .filter(dto -> dto.getAddToStats() != null && dto.getAddToStats().equals(filter.getAddToStats()))
                    .collect(Collectors.toList());
        }
        return invoiceDtos;
    }
}
