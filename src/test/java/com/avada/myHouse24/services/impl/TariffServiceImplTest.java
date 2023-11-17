package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Tariff;
import com.avada.myHouse24.model.AdditionalServiceForTariffDTO;
import com.avada.myHouse24.model.TariffDTO;
import com.avada.myHouse24.repo.TariffRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TariffServiceImplTest {
    @InjectMocks
    private TariffServiceImpl tariffService;
    @Mock
    private TariffRepository tariffRepository;
    @Test
    void getAll() {
        List<Tariff> mockTariffs = Arrays.asList(new Tariff(), new Tariff());
        when(tariffRepository.findAll()).thenReturn(mockTariffs);
        List<Tariff> result = tariffService.getAll();
        assertEquals(mockTariffs.size(), result.size());
        verify(tariffRepository, times(1)).findAll();
    }

    @Test
    void getById() {
        long mockId = 1L;
        Tariff mockTariff = new Tariff();
        when(tariffRepository.findById(mockId)).thenReturn(Optional.of(mockTariff));
        Tariff result = tariffService.getById(mockId);
        assertNotNull(result);
        verify(tariffRepository, times(1)).findById(mockId);
    }

    @Test
    void deleteById() {
        long mockId = 1L;
        tariffService.deleteById(mockId);
        verify(tariffRepository, times(1)).deleteById(mockId);
    }

    @Test
    void save() {
        TariffDTO mockTariffDto = new TariffDTO();
        mockTariffDto.setId(1L);
        mockTariffDto.setName("Mock Tariff");
        mockTariffDto.setDescription("Mock Description");
        AdditionalServiceForTariffDTO mockAdditionalServiceForTariffDto = new AdditionalServiceForTariffDTO();
        mockTariffDto.setAdditionalServiceForTariffDTOS(Collections.singletonList(mockAdditionalServiceForTariffDto));
        tariffService.save(mockTariffDto);
        verify(tariffRepository, times(1)).save(any(Tariff.class));
    }
}