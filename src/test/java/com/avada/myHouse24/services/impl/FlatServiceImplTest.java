package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.model.FlatDTO;
import com.avada.myHouse24.repo.FlatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FlatServiceImplTest {

    @InjectMocks
    private FlatServiceImpl flatService;

    @Mock
    private FlatRepository flatRepository;

    @Test
    void getById() {
        Flat mockFlat = new Flat();
        when(flatRepository.findById(1L)).thenReturn(Optional.of(mockFlat));

        Flat result = flatService.getById(1L);

        assertEquals(mockFlat, result);
        verify(flatRepository, times(1)).findById(1L);
    }

    @Test
    void getByNumber() {
        Flat mockFlat = new Flat();
        when(flatRepository.findByNumber(101)).thenReturn(Optional.of(mockFlat));

        Flat result = flatService.getByNumber(101);

        assertEquals(mockFlat, result);
        verify(flatRepository, times(1)).findByNumber(101);
    }

    @Test
    void getPage() {
        List<Flat> mockFlatList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockFlatList.add(new Flat());
        }
        when(flatRepository.findAll()).thenReturn(mockFlatList);
        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockFlatList.size());

        List<Flat> currentPageItems = mockFlatList.subList(startIndex, endIndex);

        Page<Flat> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockFlatList.size());

        Model mockModel = new ExtendedModelMap();
        when(flatRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        Page<Flat> result = flatService.getPage(0, mockModel);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
        verify(flatRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void deleteById() {
        flatService.deleteById(1L);

        verify(flatRepository, times(1)).deleteById(1L);
    }

    @Test
    void save() {
        Flat mockFlat = new Flat();
        flatService.save(mockFlat);

        verify(flatRepository, times(1)).save(mockFlat);
    }

    @Test
    void getAll() {
        List<Flat> mockFlatList = new ArrayList<>();
        when(flatRepository.findAll()).thenReturn(mockFlatList);

        List<Flat> result = flatService.getAll();

        assertEquals(mockFlatList, result);
        verify(flatRepository, times(1)).findAll();
    }

    @Test
    void getMaxId() {
        when(flatRepository.findMaxId()).thenReturn(10L);

        int result = flatService.getMaxId();

        assertEquals(10, result);
        verify(flatRepository, times(1)).findMaxId();
    }

    @Test
    void filter() {
        FlatDTO mockFlatDTO = new FlatDTO();
        mockFlatDTO.setNumber(1L);
        mockFlatDTO.setArea(1.0);
        mockFlatDTO.setHouse(new House());
        mockFlatDTO.setSection(new Section());
        mockFlatDTO.setFloor(new Floor());
        mockFlatDTO.setUser(new User());
        mockFlatDTO.setTariff(new Tariff());
        mockFlatDTO.setScoreNumber("1");
        mockFlatDTO.setBalance(-1L);

        FlatDTO mockFlatDTO1 = new FlatDTO();
        mockFlatDTO1.setNumber(1L);
        mockFlatDTO1.setArea(1.0);
        mockFlatDTO1.setHouse(new House());
        mockFlatDTO1.setSection(new Section());
        mockFlatDTO1.setFloor(new Floor());
        mockFlatDTO1.setUser(new User());
        mockFlatDTO1.setTariff(new Tariff());
        mockFlatDTO1.setScoreNumber("2");

        List<FlatDTO> mockFlatDTOList = new ArrayList<>();
        mockFlatDTOList.add(mockFlatDTO);
        mockFlatDTOList.add(mockFlatDTO1);

        List<FlatDTO> result = flatService.filter(mockFlatDTO, mockFlatDTOList, true);
        mockFlatDTOList.get(0).setBalance(1L);
        List<FlatDTO> result1 = flatService.filter(mockFlatDTO, mockFlatDTOList, false);

        assertEquals(1, result.size());
        assertEquals(1, result1.size());
    }
}