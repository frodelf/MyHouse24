package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.Invoice;
import com.avada.myHouse24.entity.InvoiceAdditionalService;
import com.avada.myHouse24.model.InvoiceDto;
import com.avada.myHouse24.model.Select2Option;
import com.avada.myHouse24.services.impl.FlatServiceImpl;
import com.avada.myHouse24.services.impl.InvoiceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {
    private final FlatServiceImpl flatService;
    public InvoiceDto toDto(Invoice invoice) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(invoice.getId());
        invoiceDto.setNumber(invoice.getNumber());
        invoiceDto.setDate(invoice.getDate());
        invoiceDto.setToDate(invoice.getToDate());
        invoiceDto.setFromDate(invoice.getFromDate());
        invoiceDto.setFlat(invoice.getFlat());
        invoiceDto.setUser(invoice.getFlat().getUser());
        invoiceDto.setScore(invoice.getScore());
        invoiceDto.setAddToStats(invoice.isAddToStats());
        invoiceDto.setStatus(invoice.getStatus());
        invoiceDto.setTariff(invoice.getTariff());
        invoiceDto.setInvoiceAdditionalServices(invoice.getInvoiceAdditionalServices());
        return invoiceDto;
    }

    public Invoice toEntity(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();
        invoice.setId(invoiceDto.getId());
        invoice.setNumber(invoiceDto.getNumber());
        invoice.setDate(invoiceDto.getDate());
        invoice.setToDate(invoiceDto.getToDate());
        invoice.setFromDate(invoiceDto.getFromDate());
        invoice.setFlat(invoiceDto.getFlat());
        invoice.setScore(invoiceDto.getScore());
        invoice.setAddToStats(invoiceDto.getAddToStats());
        invoice.setStatus(invoiceDto.getStatus());
        invoice.setTariff(invoiceDto.getTariff());
        invoice.setInvoiceAdditionalServices(invoiceDto.getInvoiceAdditionalServices());
        double sum = 0;
        for (InvoiceAdditionalService invoiceAdditionalService : invoiceDto.getInvoiceAdditionalServices()) {
            sum += invoiceAdditionalService.getConsumption() * invoiceAdditionalService.getPrice();
        }
        invoice.setSum(sum);
        return invoice;
    }

    public List<InvoiceDto> toDtoList(List<Invoice> invoices) {
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceDtos.add(toDto(invoice));
        }
        return invoiceDtos;
    }
    public List<Invoice> toEntityList(List<InvoiceDto> invoiceDtos) {
        List<Invoice> invoices = new ArrayList<>();
        for (InvoiceDto invoiceDto : invoiceDtos) {
            invoices.add(toEntity(invoiceDto));
        }
        return invoices;
    }
}