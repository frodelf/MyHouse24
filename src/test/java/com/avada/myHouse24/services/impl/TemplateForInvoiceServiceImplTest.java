package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.TemplateForInvoice;
import com.avada.myHouse24.repo.TemplateForInvoiceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TemplateForInvoiceServiceImplTest {
    @InjectMocks
    private TemplateForInvoiceServiceImpl templateForInvoiceService;

    @Mock
    private TemplateForInvoiceRepository templateForInvoiceRepository;
    @Test
    void save() {
        TemplateForInvoice mockTemplate = new TemplateForInvoice();
        templateForInvoiceService.save(mockTemplate);
        verify(templateForInvoiceRepository, times(1)).save(mockTemplate);
    }

    @Test
    void getById() {
        long mockId = 1L;
        TemplateForInvoice mockTemplate = new TemplateForInvoice();
        when(templateForInvoiceRepository.findById(mockId)).thenReturn(Optional.of(mockTemplate));
        TemplateForInvoice result = templateForInvoiceService.getById(mockId);
        assertNotNull(result);
        verify(templateForInvoiceRepository, times(1)).findById(mockId);
    }

    @Test
    void deleteById() {
        long mockId = 1L;
        templateForInvoiceService.deleteById(mockId);
        verify(templateForInvoiceRepository, times(1)).deleteById(mockId);
    }

    @Test
    void getAll() {
        List<TemplateForInvoice> mockTemplates = Arrays.asList(new TemplateForInvoice(), new TemplateForInvoice());
        when(templateForInvoiceRepository.findAll()).thenReturn(mockTemplates);
        List<TemplateForInvoice> result = templateForInvoiceService.getAll();
        assertEquals(mockTemplates.size(), result.size());
        verify(templateForInvoiceRepository, times(1)).findAll();
    }

    @Test
    void getTemplateWhereIsMainIsTrue() {
        TemplateForInvoice mockTemplate = new TemplateForInvoice();
        when(templateForInvoiceRepository.findByIsMainIsTrue()).thenReturn(mockTemplate);
        TemplateForInvoice result = templateForInvoiceService.getTemplateWhereIsMainIsTrue();
        assertNotNull(result);
        verify(templateForInvoiceRepository, times(1)).findByIsMainIsTrue();
    }
}