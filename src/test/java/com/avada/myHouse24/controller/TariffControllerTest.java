package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.entity.Tariff;
import com.avada.myHouse24.entity.UnitOfMeasurement;
import com.avada.myHouse24.mapper.AdditionalServiceMapper;
import com.avada.myHouse24.mapper.TariffMapper;
import com.avada.myHouse24.model.AdditionalServiceForTariffDTO;
import com.avada.myHouse24.model.TariffDTO;
import com.avada.myHouse24.services.impl.AdditionalServiceImpl;
import com.avada.myHouse24.services.impl.TariffServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TariffControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdditionalServiceImpl additionalService;

    @MockBean
    private TariffServiceImpl tariffService;

    @MockBean
    private AdditionalServiceMapper additionalServiceMapper;

    @MockBean
    private TariffMapper tariffMapper;

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void index() throws Exception {
        when(tariffService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tariff/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tariff/get-all"))
                .andExpect(model().attributeExists("tariffs"));

        verify(tariffService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void add() throws Exception {
        when(additionalServiceMapper.toDtoList(anyList())).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tariff/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/tariff/add"))
                .andExpect(model().attributeExists("services"));

        verify(additionalService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testAdd() throws Exception {
        when(additionalService.getByName(anyString())).thenReturn(new AdditionalService());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tariff/add")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("services"));

        TariffDTO tariffDTO = new TariffDTO();
        tariffDTO.setAdditionalServiceForTariffDTOS(new ArrayList<>());
        tariffDTO.setName("name");
        tariffDTO.setNames(Arrays.asList("service1"));
        AdditionalServiceForTariffDTO additionalServiceForTariffDTO = new AdditionalServiceForTariffDTO();
        additionalServiceForTariffDTO.setPrice(12L);
        additionalServiceForTariffDTO.setAdditionalService(new AdditionalService());
        tariffDTO.setAdditionalServiceForTariffDTOS(Arrays.asList(additionalServiceForTariffDTO));
        tariffDTO.setDescription("description");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tariff/add")
                        .flashAttr("tariffDTO", tariffDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tariff/index"));

        verify(additionalService, times(1)).getByName(anyString());
        verify(tariffService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void edit() throws Exception {
//        TariffDTO tariffDTO = new TariffDTO();
//        tariffDTO.setAdditionalServiceForTariffDTOS(new ArrayList<>());
//        when(additionalServiceMapper.toDtoList(anyList())).thenReturn(new ArrayList<>());
//        when(tariffMapper.toDto(any())).thenReturn(tariffDTO);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tariff/edit/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("admin/tariff/edit"))
//                .andExpect(model().attributeExists("services", "tariffDTO", "index"));
//
//        verify(additionalService, times(1)).getAll();
//        verify(tariffService, times(2)).getById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testEdit() throws Exception {
//        when(additionalService.getByName(anyString())).thenReturn(new AdditionalService());
//        when(tariffService.getById(anyLong())).thenReturn(new Tariff());
//        when(tariffMapper.toDto(any())).thenReturn(new TariffDTO());
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tariff/edit/1")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("tariffDTO"));
//
//        TariffDTO tariffDTO = new TariffDTO();
//        tariffDTO.setName("name");
//        tariffDTO.setDescription("description");
//        tariffDTO.setNames(Arrays.asList("asdf"));
//        AdditionalServiceForTariffDTO additionalServiceForTariffDTO = new AdditionalServiceForTariffDTO();
//        additionalServiceForTariffDTO.setPrice(12L);
//        additionalServiceForTariffDTO.setAdditionalService(new AdditionalService());
//        tariffDTO.setAdditionalServiceForTariffDTOS(Arrays.asList(additionalServiceForTariffDTO));
//        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tariff/edit/1")
//                        .flashAttr("tariffDTO", tariffDTO)
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/admin/tariff/index"));
//
//        verify(additionalService, times(1)).getByName(anyString());
//        verify(tariffService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getById() throws Exception {
        when(tariffMapper.toDto(any())).thenReturn(new TariffDTO());
        when(tariffService.getById(anyLong())).thenReturn(new Tariff());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tariff/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/tariff/index"))
                .andExpect(model().attributeExists("tariffDTO"));

        verify(tariffService, times(2)).getById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tariff/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/tariff/index"));

        verify(tariffService, times(1)).deleteById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void cloneById() throws Exception {
//        TariffDTO tariffDTO = new TariffDTO();
//        tariffDTO.setName("name");
//        tariffDTO.setDescription("description");
//        tariffDTO.setNames(Arrays.asList("asdf"));
//        AdditionalServiceForTariffDTO additionalServiceForTariffDTO = new AdditionalServiceForTariffDTO();
//        additionalServiceForTariffDTO.setPrice(12L);
//        AdditionalService additionalService1 = new AdditionalService();
//        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
//        unitOfMeasurement.setName("unit name");
//        additionalService1.setUnitOfMeasurement(unitOfMeasurement);
//        additionalServiceForTariffDTO.setAdditionalService(additionalService1);
//        tariffDTO.setAdditionalServiceForTariffDTOS(Arrays.asList(additionalServiceForTariffDTO));
//        when(additionalServiceMapper.toDtoList(anyList())).thenReturn(new ArrayList<>());
//        when(tariffMapper.toDto(any())).thenReturn(tariffDTO);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tariff/clone/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("/admin/tariff/edit"))
//                .andExpect(model().attributeExists("services", "tariffDTO", "index"));
//
//        verify(additionalService, times(1)).getAll();
//        verify(tariffService, times(2)).getById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testCloneById() throws Exception {
//        TariffDTO tariffDTO = new TariffDTO();
//        tariffDTO.setName("name");
//        tariffDTO.setDescription("description");
//        tariffDTO.setNames(Arrays.asList("asdf"));
//        AdditionalServiceForTariffDTO additionalServiceForTariffDTO = new AdditionalServiceForTariffDTO();
//        additionalServiceForTariffDTO.setPrice(12L);
//        AdditionalService additionalService1 = new AdditionalService();
//        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
//        unitOfMeasurement.setName("unit name");
//        additionalService1.setUnitOfMeasurement(unitOfMeasurement);
//        additionalServiceForTariffDTO.setAdditionalService(additionalService1);
//        tariffDTO.setAdditionalServiceForTariffDTOS(Arrays.asList(additionalServiceForTariffDTO));
//
//        when(additionalService.getByName(anyString())).thenReturn(new AdditionalService());
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tariff/copy")
//                        .flashAttr("tariffDTO", tariffDTO)
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/admin/tariff/index"));
//        additionalServiceForTariffDTO = new AdditionalServiceForTariffDTO();
//        additionalServiceForTariffDTO.setPrice(12L);
//        additionalService1 = new AdditionalService();
//        unitOfMeasurement = new UnitOfMeasurement();
//        unitOfMeasurement.setName("unit name");
//        additionalService1.setUnitOfMeasurement(unitOfMeasurement);
//        additionalServiceForTariffDTO.setAdditionalService(additionalService1);
//        tariffDTO.setAdditionalServiceForTariffDTOS(Arrays.asList(additionalServiceForTariffDTO));
//        tariffDTO.setDescription(null);
//        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tariff/copy")
//                        .flashAttr("tariffDTO", tariffDTO)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("tariffDTO", "services", "index"));
//
//        verify(additionalService, times(1)).getByName(anyString());
//        verify(tariffService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getTariffById() throws Exception {
        Tariff tariff = new Tariff();
        tariff.setId(1L);
        when(tariffService.getById(anyLong())).thenReturn(tariff);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tariff/getTariffById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(tariffService, times(1)).getById(anyLong());
    }
}