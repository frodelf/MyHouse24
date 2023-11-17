package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Floor;
import com.avada.myHouse24.repo.FloorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FloorServiceImplTest {
    @InjectMocks
    private FloorServiceImpl floorService;

    @Mock
    private FloorRepository floorRepository;

    @Test
    void getById() {
        Floor mockFloor = new Floor();
        when(floorRepository.findById(1L)).thenReturn(Optional.of(mockFloor));

        Floor result = floorService.getById(1L);

        assertEquals(mockFloor, result);
        verify(floorRepository, times(1)).findById(1L);
    }

    @Test
    void getAll() {
        List<Floor> mockFloorList = new ArrayList<>();
        when(floorRepository.findAll()).thenReturn(mockFloorList);

        List<Floor> result = floorService.getAll();

        assertEquals(mockFloorList, result);
        verify(floorRepository, times(1)).findAll();
    }

    @Test
    void save() {
        Floor mockFloor = new Floor();
        floorService.save(mockFloor);

        verify(floorRepository, times(1)).save(mockFloor);
    }

    @Test
    void deleteById() {
        floorService.deleteById(1L);

        verify(floorRepository, times(1)).deleteById(1L);
    }
}