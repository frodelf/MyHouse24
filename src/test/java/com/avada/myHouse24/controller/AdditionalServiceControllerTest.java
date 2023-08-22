package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.mapper.AdditionalServiceMapper;
import com.avada.myHouse24.mapper.UnitOfMeasurementMapper;
import com.avada.myHouse24.model.AdditionalServiceDTO;
import com.avada.myHouse24.model.AdditionalServiceListDTO;
import com.avada.myHouse24.model.UnitOfMeasurementDTO;
import com.avada.myHouse24.services.impl.AdditionalServiceImpl;
import com.avada.myHouse24.services.impl.UnitOfMeasurementServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdditionalServiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdditionalServiceMapper additionalServiceMapper;

    @MockBean
    private AdditionalServiceImpl additionalService;

    @MockBean
    private UnitOfMeasurementServiceImpl unitOfMeasurementService;

    @MockBean
    private UnitOfMeasurementMapper unitOfMeasurementMapper;

    @Test
    void getAll() throws Exception {
        when(additionalService.getAll()).thenReturn(Collections.emptyList());
        when(unitOfMeasurementService.getAll()).thenReturn(Collections.emptyList());
        when(additionalServiceMapper.toDtoList(anyList())).thenReturn(new ArrayList<>());
        when(unitOfMeasurementMapper.toDtoList(anyList())).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/service/index"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("services", "units"));

        verify(additionalService, times(1)).getAll();
        verify(unitOfMeasurementService, times(1)).getAll();
    }

    @Test
    void add() throws Exception {
        AdditionalServiceListDTO additionalServiceListDTO = new AdditionalServiceListDTO();
        when(unitOfMeasurementMapper.toEntityList(anyList())).thenReturn(new ArrayList<>());
        when(additionalServiceMapper.toEntityList(anyList())).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/service/add")
                        .flashAttr("additionalServiceListDTO", additionalServiceListDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/service/index"));

        verify(unitOfMeasurementService, times(1)).saveList(anyList());
        verify(additionalService, times(1)).saveList(anyList());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/service/delete/service/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/service/index"));

        verify(additionalService, times(1)).deleteById(1L);
    }

    @Test
    void getAllHouses() throws Exception {
        int page = 1;
        String search = "";
        int pageSize = 10;

        List<AdditionalService> mockAdditionalServices = new ArrayList<>();
        AdditionalService additionalService1 = new AdditionalService();
        AdditionalService additionalService2 = new AdditionalService();
        additionalService1.setId(1L);
        additionalService2.setId(2L);
        additionalService1.setName("Service 1");
        additionalService2.setName("Service 2");
        mockAdditionalServices.add(additionalService1);
        mockAdditionalServices.add(additionalService2);

        when(additionalService.forSelect(page, pageSize, search)).thenReturn(mockAdditionalServices);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/service/get-additional-services")
                        .param("_page", String.valueOf(page))
                        .param("_search", search))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].id").value(1))
                .andExpect(jsonPath("$.results[0].text").value("Service 1"))
                .andExpect(jsonPath("$.results[1].id").value(2))
                .andExpect(jsonPath("$.results[1].text").value("Service 2"))
                .andExpect(jsonPath("$.pagination.more").value(false));

        verify(additionalService, times(1)).forSelect(page, pageSize, search);
    }
}