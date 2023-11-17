package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Section;
import com.avada.myHouse24.repo.SectionRepository;
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
class SectionServiceImplTest {
    @InjectMocks
    private SectionServiceImpl sectionService;

    @Mock
    private SectionRepository sectionRepository;
    @Test
    void getByName() {
        String sectionName = "A";
        Section mockSection = new Section();
        when(sectionRepository.findByName(sectionName)).thenReturn(Optional.of(mockSection));
        Section result = sectionService.getByName(sectionName);
        assertEquals(mockSection, result);
        verify(sectionRepository, times(1)).findByName(sectionName);
    }

    @Test
    void getById() {
        long sectionId = 1L;
        Section mockSection = new Section();
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(mockSection));
        Section result = sectionService.getById(sectionId);
        assertEquals(mockSection, result);
        verify(sectionRepository, times(1)).findById(sectionId);
    }

    @Test
    void save() {
        Section sectionToSave = new Section();
        sectionService.save(sectionToSave);
        verify(sectionRepository, times(1)).save(sectionToSave);
    }

    @Test
    void getAll() {
        List<Section> mockSections = Arrays.asList(new Section(), new Section());
        when(sectionRepository.findAll()).thenReturn(mockSections);
        List<Section> result = sectionService.getAll();
        assertEquals(mockSections, result);
        verify(sectionRepository, times(1)).findAll();
    }

    @Test
    void getMaxId() {
        long mockMaxId = 10L;
        when(sectionRepository.findMaxId()).thenReturn(mockMaxId);
        long result = sectionService.getMaxId();
        assertEquals(mockMaxId, result);
        verify(sectionRepository, times(1)).findMaxId();
    }
}