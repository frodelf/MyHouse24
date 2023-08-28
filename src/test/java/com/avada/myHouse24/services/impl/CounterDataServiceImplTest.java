package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.mapper.CounterDataMapper;
import com.avada.myHouse24.model.CounterDataDTO;
import com.avada.myHouse24.model.CounterDataFilterDto;
import com.avada.myHouse24.repo.CounterDataRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CounterDataServiceImplTest {
    @InjectMocks
    private CounterDataServiceImpl counterDataService;

    @Mock
    private CounterDataRepository counterDataRepository;

    @Mock
    private CounterDataMapper counterDataMapper;

    @Mock
    private SectionServiceImpl sectionService;

    @Mock
    private FlatServiceImpl flatService;

    @Mock
    private HouseServiceImpl houseService;

    @Mock
    private AdditionalServiceImpl additionalService;
    @Test
    void getPage() {
        List<CounterData> mockCounterDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockCounterDataList.add(new CounterData());
        }
        when(counterDataRepository.findAll()).thenReturn(mockCounterDataList);

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockCounterDataList.size());

        List<CounterData> currentPageItems = mockCounterDataList.subList(startIndex, endIndex);

        Page<CounterData> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockCounterDataList.size());

        Model mockModel = new ExtendedModelMap();
        when(counterDataRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        Page<CounterData> result = counterDataService.getPage(0, mockModel);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
        verify(counterDataRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetPage() {
        List<CounterDataDTO> mockCounterDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockCounterDataList.add(new CounterDataDTO());
        }
        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockCounterDataList.size());

        List<CounterDataDTO> currentPageItems = mockCounterDataList.subList(startIndex, endIndex);

        Page<CounterDataDTO> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockCounterDataList.size());

        Model mockModel = new ExtendedModelMap();
        Page<CounterDataDTO> result = counterDataService.getPage(1, mockModel, mockCounterDataList);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
    }

    @Test
    void getById() {
        CounterData mockCounterData = new CounterData();
        when(counterDataRepository.findById(1L)).thenReturn(Optional.of(mockCounterData));

        CounterData result = counterDataService.getById(1L);

        assertEquals(mockCounterData, result);
        verify(counterDataRepository, times(1)).findById(1L);
    }

    @Test
    void save() {
        CounterData mockCounterData = new CounterData();
        counterDataService.save(mockCounterData);

        verify(counterDataRepository, times(1)).save(mockCounterData);
    }

    @Test
    void deleteById() {
        counterDataService.deleteById(1L);

        verify(counterDataRepository, times(1)).deleteById(1L);
    }

    @Test
    void getMaxId() {
        when(counterDataRepository.findMaxId()).thenReturn(10L);

        long result = counterDataService.getMaxId();

        assertEquals(10L, result);
        verify(counterDataRepository, times(1)).findMaxId();
    }

    @Test
    void updateFilter() {
        CounterDataFilterDto filterDto = new CounterDataFilterDto();
        filterDto.setHouse(1L);
        filterDto.setFlat(1L);
        filterDto.setSection(1L);
        filterDto.setAdditionalService(1L);

        House house = new House();
        house.setId(1L);
        house.setName("house name");
        Flat flat = new Flat();
        flat.setId(1L);
        flat.setNumber(1111);
        Section section = new Section();
        section.setId(1L);
        section.setName("section name");
        AdditionalService additionalService1 = new AdditionalService();
        additionalService1.setId(1L);
        additionalService1.setName("additional name");

        when(houseService.getById(anyLong())).thenReturn(house);
        when(flatService.getById(anyLong())).thenReturn(flat);
        when(sectionService.getById(anyLong())).thenReturn(section);
        when(additionalService.getById(anyLong())).thenReturn(additionalService1);

        CounterDataFilterDto result = counterDataService.updateFilter(filterDto);

        assertEquals(result.getHouseName(), "house name");
        assertEquals(result.getFlatName(), "1111");
        assertEquals(result.getSectionName(), "section name");
        assertEquals(result.getAdditionalServiceName(), "additional name");
        verify(houseService, times(1)).getById(1L);
    }

    @Test
    void existNumber() {
        when(counterDataRepository.existsByNumber("12345")).thenReturn(true);

        boolean result = counterDataService.existNumber("12345");

        assertTrue(result);
        verify(counterDataRepository, times(1)).existsByNumber("12345");
    }

    @Test
    void filter() {
        CounterDataFilterDto filterDto = new CounterDataFilterDto();
        filterDto.setHouse(1L);
        filterDto.setFlat(1L);
        filterDto.setSection(1L);
        filterDto.setAdditionalService(1L);

        CounterDataDTO mockDto1 = new CounterDataDTO();
        CounterDataDTO mockDto2 = new CounterDataDTO();
        Flat flat = new Flat();
        flat.setId(1L);
        House house = new House();
        house.setId(1L);
        Section section = new Section();
        section.setId(1L);

        AdditionalService additionalService1 = new AdditionalService();
        AdditionalService additionalService2 = new AdditionalService();
        additionalService1.setId(1L);
        additionalService2.setId(1L);
        mockDto1.setAdditionalService(additionalService1);
        mockDto2.setAdditionalService(additionalService2);
        mockDto1.setFlat(flat);
        mockDto2.setFlat(flat);

        flat.setSection(section);
        flat.setHouse(house);
        mockDto1.setFlat(flat);
        mockDto2.setFlat(flat);
        when(counterDataMapper.toDtoList(anyList())).thenReturn(Arrays.asList(mockDto1, mockDto2));

        List<CounterDataDTO> result = counterDataService.filter(filterDto);

        assertEquals(2, result.size());
        verify(counterDataMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void filterWithParams() {
        CounterDataFilterDto filterDto = new CounterDataFilterDto();
        filterDto.setHouse(1L);
        filterDto.setFlat(1L);
        filterDto.setSection(1L);
        filterDto.setAdditionalService(1L);

        CounterDataDTO mockDto1 = new CounterDataDTO();
        CounterDataDTO mockDto2 = new CounterDataDTO();
        mockDto1.setFlat(new Flat());
        mockDto2.setFlat(new Flat());
        List<CounterDataDTO> mockDtoList = Arrays.asList(mockDto1, mockDto2);
        when(counterDataMapper.toDtoList(anyList())).thenReturn(mockDtoList);

        List<CounterData> mockCounterDataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockCounterDataList.add(new CounterData());
        }

        Date mockDate = new Date(1,1,1);
        List<CounterData> filteredCounterData = new ArrayList<>(mockCounterDataList.subList(0, 3));
        when(counterDataRepository.findAll()).thenReturn(mockCounterDataList);
        when(counterDataMapper.toDtoList(anyList())).thenReturn(mockDtoList);

        List<CounterDataDTO> result = counterDataService.filter(filterDto, "status", "number", mockDate, filteredCounterData);

        assertEquals(2, result.size());
        verify(counterDataMapper, times(1)).toDtoList(anyList());
    }
}