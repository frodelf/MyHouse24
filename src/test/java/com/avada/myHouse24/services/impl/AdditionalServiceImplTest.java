package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.repo.AdditionalServiceRepository;
import com.avada.myHouse24.repo.CounterDataRepository;
import com.avada.myHouse24.repo.InvoiceAdditionalServiceRepository;
import com.avada.myHouse24.repo.TariffAdditionalServiceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdditionalServiceImplTest {
    @InjectMocks
    private AdditionalServiceImpl additionalService;
    @Mock
    private AdditionalServiceRepository additionalServiceRepository;
    @Mock
    private CounterDataRepository counterDataRepository;
    @Mock
    private TariffAdditionalServiceRepository tariffAdditionalServiceRepository;
    @Mock
    private InvoiceAdditionalServiceRepository invoiceAdditionalServiceRepository;

    @Test
    void getAll() {
        List<AdditionalService> mockServices = new ArrayList<>();
        when(additionalServiceRepository.findAll()).thenReturn(mockServices);

        List<AdditionalService> result = additionalService.getAll();

        assertEquals(mockServices, result);
        verify(additionalServiceRepository, times(1)).findAll();
    }

    @Test
    void save() {
        AdditionalService mockService = new AdditionalService();
        additionalService.save(mockService);
        verify(additionalServiceRepository, times(1)).save(mockService);
    }

    @Test
    void saveList() {
        List<AdditionalService> mockServices = new ArrayList<>();
        mockServices.add(new AdditionalService());
        mockServices.add(new AdditionalService());

        additionalService.saveList(mockServices);

        verify(additionalServiceRepository, times(2)).save(any(AdditionalService.class));
    }

    @Test
    void deleteById() {
        when(additionalService.isUses(anyLong())).thenReturn(false);
        additionalService.deleteById(1L);
        verify(additionalServiceRepository, times(1)).deleteById(1L);
    }

    @Test
    void getById() {
        AdditionalService mockService = new AdditionalService();
        mockService.setId(1L);
        when(additionalServiceRepository.findById(1L)).thenReturn(Optional.of(mockService));

        AdditionalService result = additionalService.getById(1L);

        assertEquals(mockService, result);
        verify(additionalServiceRepository, times(1)).findById(1L);
    }

    @Test
    void getByName() {
        String serviceName = "Cleaning";
        AdditionalService mockService = new AdditionalService();
        mockService.setName(serviceName);
        when(additionalServiceRepository.findByName(serviceName)).thenReturn(Optional.of(mockService));

        AdditionalService result = additionalService.getByName(serviceName);

        assertEquals(mockService, result);
        verify(additionalServiceRepository, times(1)).findByName(serviceName);
    }

    @Test
    void forSelect() {
        List<AdditionalService> mockServices = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockServices.add(new AdditionalService());
        }
        Page<AdditionalService> mockPage = new PageImpl<>(mockServices.subList(0, 10));
        when(additionalServiceRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
        when(additionalServiceRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(mockPage);

        List<AdditionalService> result = additionalService.forSelect(1, 10, null);

        assertEquals(mockPage.getContent(), result);
        verify(additionalServiceRepository, times(1)).findAll(any(Pageable.class));

        List<AdditionalService> searchResult = additionalService.forSelect(1, 10, "cleaning");

        assertEquals(mockPage.getContent(), searchResult);
        verify(additionalServiceRepository, times(1)).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }
}