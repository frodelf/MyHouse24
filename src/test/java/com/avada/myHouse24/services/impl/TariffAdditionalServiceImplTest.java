package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.entity.Tariff;
import com.avada.myHouse24.entity.TariffAdditionalService;
import com.avada.myHouse24.model.AdditionalServiceForTariffDTO;
import com.avada.myHouse24.repo.TariffAdditionalServiceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TariffAdditionalServiceImplTest {
    @InjectMocks
    private TariffAdditionalServiceImpl tariffAdditionalService;
    @Mock
    private TariffAdditionalServiceRepository tariffAdditionalServiceRepository;
    @Test
    void getAllAdditionalServiceByTariff() {
        Tariff mockTariff = new Tariff();
        com.avada.myHouse24.entity.TariffAdditionalService mockTariffAdditionalService1 = new com.avada.myHouse24.entity.TariffAdditionalService();
        mockTariffAdditionalService1.setAdditionalService(new AdditionalService());
        mockTariffAdditionalService1.setPrice(50L);
        com.avada.myHouse24.entity.TariffAdditionalService mockTariffAdditionalService2 = new com.avada.myHouse24.entity.TariffAdditionalService();
        mockTariffAdditionalService2.setAdditionalService(new AdditionalService());
        mockTariffAdditionalService2.setPrice(75L);
        List<TariffAdditionalService> mockTariffAdditionalServices = Arrays.asList(mockTariffAdditionalService1, mockTariffAdditionalService2);
        when(tariffAdditionalServiceRepository.findAllByTariff(mockTariff)).thenReturn(mockTariffAdditionalServices);

        List<AdditionalServiceForTariffDTO> result = tariffAdditionalService.getAllAdditionalServiceByTariff(mockTariff);

        assertEquals(mockTariffAdditionalServices.size(), result.size());
        verify(tariffAdditionalServiceRepository, times(1)).findAllByTariff(mockTariff);
    }

    @Test
    void deleteAllByTariff() {
        Tariff mockTariff = new Tariff();
        tariffAdditionalService.deleteAllByTariff(mockTariff);
        verify(tariffAdditionalServiceRepository, times(1)).deleteAllByTariff(mockTariff);
    }
}