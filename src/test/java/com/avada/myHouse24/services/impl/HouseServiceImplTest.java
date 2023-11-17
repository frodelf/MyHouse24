package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.model.HouseForAddDto;
import com.avada.myHouse24.model.HouseForViewDto;
import com.avada.myHouse24.repo.HouseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class HouseServiceImplTest {
    @InjectMocks
    private HouseServiceImpl houseService;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private AdminServiceImpl adminService;
    @Test
    void getByName() {
        House expectedHouse = new House();
        when(houseRepository.findByName("TestHouseName")).thenReturn(Optional.of(expectedHouse));
        House result = houseService.getByName("TestHouseName");
        assertEquals(expectedHouse, result);
    }

    @Test
    void getById() {
        House expectedHouse = new House();
        when(houseRepository.findById(1L)).thenReturn(Optional.of(expectedHouse));
        House result = houseService.getById(1L);
        assertEquals(expectedHouse, result);
    }

    @Test
    void getAll() {
        List<House> expectedHouses = new ArrayList<>();
        expectedHouses.add(new House());
        expectedHouses.add(new House());
        when(houseRepository.findAll()).thenReturn(expectedHouses);
        List<House> result = houseService.getAll();
        assertEquals(expectedHouses, result);
    }

    @Test
    void getAllName() {
        List<House> expectedHouses = new ArrayList<>();
        expectedHouses.add(new House());
        expectedHouses.add(new House());
        when(houseRepository.findAll()).thenReturn(expectedHouses);
        List<String> result = houseService.getAllName();
        assertEquals(2, result.size());
    }

    @Test
    void getPage() {
        int pageNumber = 1;
        List<House> mockHouseList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockHouseList.add(new House());
        }
        when(houseRepository.findAll()).thenReturn(mockHouseList);

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockHouseList.size());

        List<House> currentPageItems = mockHouseList.subList(startIndex, endIndex);

        Page<House> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockHouseList.size());

        Model mockModel = new ExtendedModelMap();
        when(houseRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        Page<House> result = houseService.getPage(0, mockModel);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
        verify(houseRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetPage() {
        int pageNumber = 1;
        List<House> mockHouseList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockHouseList.add(new House());
        }

        List<HouseForViewDto> houseForViewDtos = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            houseForViewDtos.add(new HouseForViewDto());
        }

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, houseForViewDtos.size());

        List<HouseForViewDto> currentPageItems = houseForViewDtos.subList(startIndex, endIndex);

        Page<HouseForViewDto> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), houseForViewDtos.size());

        Model mockModel = new ExtendedModelMap();

        Page<HouseForViewDto> result = houseService.getPage(0, mockModel, houseForViewDtos);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
    }

    @Test
    void deleteById() {
        doNothing().when(houseRepository).deleteById(1L);
        houseService.deleteById(1L);
        verify(houseRepository, times(1)).deleteById(1L);
    }

    @Test
    void add() throws IOException {
        HouseForAddDto houseDto = new HouseForAddDto();
        houseDto.setId(1L);
        houseDto.setSections(Arrays.asList("section"));
        houseDto.setFloors(Arrays.asList("floor"));
        houseDto.setUsers(Arrays.asList("users"));
        houseDto.setImage(new MockMultipartFile("0","0","0", new byte[1]));
        houseDto.setImage1(new MockMultipartFile("1","0","0", new byte[1]));
        houseDto.setImage2(new MockMultipartFile("2","0","0", new byte[1]));
        houseDto.setImage3(new MockMultipartFile("3","0","0", new byte[1]));
        houseDto.setImage4(new MockMultipartFile("4","0","0", new byte[1]));
        House house = new House();
        house.setSections(Arrays.asList(new Section()));
        house.setFloors(Arrays.asList(new Floor()));
        house.setAdmins(Arrays.asList(new Admin()));
        when(houseRepository.findById(anyLong())).thenReturn(Optional.of(house));
        when(adminService.getByName(anyString())).thenReturn(new Admin());
        when(houseRepository.save(any(House.class))).thenReturn(new House());
        houseService.add(houseDto);
        houseDto.setId(null);
        houseService.add(houseDto);
        verify(houseRepository, times(2)).save(any(House.class));
    }

    @Test
    void forSelect() {
        int page = 1;
        int pageSize = 10;
        String search = "TestSearch";
        Page<House> expectedPage = new PageImpl<>(new ArrayList<>());
        when(houseRepository.findByNameContainingIgnoreCase(eq(search), any(Pageable.class))).thenReturn(expectedPage);
        when(houseRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);
        List<House> result1 = houseService.forSelect(page, pageSize, search);
        List<House> result2 = houseService.forSelect(page, pageSize, null);
        verify(houseRepository, times(1)).findByNameContainingIgnoreCase(eq(search), any(Pageable.class));
        verify(houseRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void filter() {
        HouseForViewDto filterDto = new HouseForViewDto();
        filterDto.setName("TestName");
        filterDto.setAddress("TestAddress");

        HouseForViewDto house = new HouseForViewDto();
        house.setName("TestName");
        house.setAddress("TestAddress");
        List<HouseForViewDto> houses = new ArrayList<>();
        houses.add(house);
        houses.add(new HouseForViewDto());

        List<HouseForViewDto> filteredHouses = houseService.filter(filterDto, houses);

        assertEquals(1, filteredHouses.size());
        assertEquals("TestName", filteredHouses.get(0).getName());
    }
}