package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.Invoice;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.mapper.InvoiceMapper;
import com.avada.myHouse24.model.InvoiceDto;
import com.avada.myHouse24.repo.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class InvoiceServiceImplTest {
    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;
    @Test
    void getAll() {
        List<Invoice> mockInvoiceList = new ArrayList<>();
        when(invoiceRepository.findAll()).thenReturn(mockInvoiceList);

        List<Invoice> result = invoiceService.getAll();

        assertEquals(mockInvoiceList, result);
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void getById() {
        Invoice mockInvoice = new Invoice();
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(mockInvoice));

        Invoice result = invoiceService.getById(1L);

        assertEquals(mockInvoice, result);
        verify(invoiceRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById() {
        invoiceService.deleteById(1L);

        verify(invoiceRepository, times(1)).deleteById(1L);
    }

    @Test
    void save() {
        Invoice mockInvoice = new Invoice();
        invoiceService.save(mockInvoice);

        verify(invoiceRepository, times(1)).save(mockInvoice);
    }

    @Test
    void getPage() {
        List<Invoice> mockInvoice = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockInvoice.add(new Invoice());
        }
        when(invoiceRepository.findAll()).thenReturn(mockInvoice);

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockInvoice.size());

        List<Invoice> currentPageItems = mockInvoice.subList(startIndex, endIndex);

        Page<Invoice> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockInvoice.size());

        Model mockModel = new ExtendedModelMap();
        when(invoiceRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        Page<Invoice> result = invoiceService.getPage(1, mockModel);

        assertEquals(mockPage, result);
        verify(invoiceRepository, times(1)).findAll(any(PageRequest.class));
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
    }

    @Test
    void getPageDto() {
        List<InvoiceDto> mockInvoice = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockInvoice.add(new InvoiceDto());
        }

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockInvoice.size());

        List<InvoiceDto> currentPageItems = mockInvoice.subList(startIndex, endIndex);

        Page<InvoiceDto> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockInvoice.size());

        Model mockModel = new ExtendedModelMap();

        Page<InvoiceDto> result = invoiceService.getPage(1, mockModel, mockInvoice);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
    }

    @Test
    void forSelect() {
        Page<Invoice> mockPage = new PageImpl<>(new ArrayList<>());
        when(invoiceRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);
        List<Invoice> result1 = invoiceService.forSelect(1, 10, "");
        assertEquals(mockPage.getContent(), result1);
        when(invoiceRepository.findByNumberContainingIgnoreCase(anyString(), any())).thenReturn(mockPage);
        List<Invoice> result2 = invoiceService.forSelect(1, 10, "qwerty");
        assertEquals(mockPage.getContent(), result2);
        verify(invoiceRepository, times(1)).findAll(any(PageRequest.class));
        verify(invoiceRepository, times(1)).findByNumberContainingIgnoreCase(anyString(), any());
    }

    @Test
    void filter() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setNumber("1");
        invoiceDto.setStatus("1");
        invoiceDto.setMonths("2023-02");
        invoiceDto.setUser(new User());
        invoiceDto.setAddToStats(true);

        InvoiceDto invoiceDto1 = new InvoiceDto();
        invoiceDto1.setNumber("1");
        invoiceDto1.setStatus("1");
        invoiceDto1.setMonths("1");
        invoiceDto1.setUser(new User());
        invoiceDto1.setAddToStats(true);
        invoiceDto1.setDate(new Date(1,1,1));
        Flat flat = new Flat();
        flat.setId(1L);
        invoiceDto1.setFlat(flat);

        InvoiceDto invoiceDto2 = new InvoiceDto();
        invoiceDto2.setNumber("1");
        invoiceDto2.setStatus("1");
        invoiceDto2.setMonths("22");
        invoiceDto2.setUser(new User());
        invoiceDto2.setAddToStats(true);

        List<InvoiceDto> invoiceDtos = Arrays.asList(invoiceDto1, invoiceDto2);
        when(invoiceMapper.toDtoList(any())).thenReturn(invoiceDtos);
        List<InvoiceDto> result = invoiceService.filter(invoiceDto, "1", new Date(1,1,1));
        assertEquals(0, result.size());
    }
}