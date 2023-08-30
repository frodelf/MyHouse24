package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.UnitOfMeasurement;
import com.avada.myHouse24.repo.UnitOfMeasurementRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UnitOfMeasurementServiceImplTest {
    @InjectMocks
    private UnitOfMeasurementServiceImpl unitOfMeasurementService;
    @Mock
    private UnitOfMeasurementRepository unitOfMeasurementRepository;
    @Test
    void getAll() {
        List<UnitOfMeasurement> mockUnits = new ArrayList<>();
        mockUnits.add(new UnitOfMeasurement());
        mockUnits.add(new UnitOfMeasurement());
        when(unitOfMeasurementRepository.findAll()).thenReturn(mockUnits);
        List<UnitOfMeasurement> result = unitOfMeasurementService.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void save() {
        unitOfMeasurementService.save(new UnitOfMeasurement());
        verify(unitOfMeasurementRepository, times(1)).save(any());
    }

    @Test
    void saveList() {
        List<UnitOfMeasurement> unitOfMeasurements = Arrays.asList(new UnitOfMeasurement(), new UnitOfMeasurement());
        unitOfMeasurementService.saveList(unitOfMeasurements);
        verify(unitOfMeasurementRepository, times(2)).save(any());
    }

    @Test
    void deleteById() {
        long mockId = 1L;
        when(unitOfMeasurementRepository.findById(mockId)).thenReturn(Optional.of(new UnitOfMeasurement()));
        unitOfMeasurementService.deleteById(mockId);
        verify(unitOfMeasurementRepository, times(1)).deleteById(mockId);
    }

    @Test
    void getByName() {
        UnitOfMeasurement mockUnit = new UnitOfMeasurement();
        mockUnit.setName("unit name");
        when(unitOfMeasurementRepository.findByName(anyString())).thenReturn(Optional.of(mockUnit));
        UnitOfMeasurement result = unitOfMeasurementService.getByName("unit name");
        assertEquals("unit name", result.getName());
    }
}