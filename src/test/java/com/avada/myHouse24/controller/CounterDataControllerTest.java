package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.enums.CounterDataStatus;
import com.avada.myHouse24.mapper.CounterDataMapper;
import com.avada.myHouse24.model.CounterDataDTO;
import com.avada.myHouse24.model.CounterDataFilterDto;
import com.avada.myHouse24.repo.AdminRepository;
import com.avada.myHouse24.services.impl.CounterDataServiceImpl;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.validator.CounterDataValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CounterDataControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HouseServiceImpl houseService;

    @MockBean
    private CounterDataServiceImpl counterDataService;
    @MockBean
    private CounterDataMapper counterDataMapper;

    @MockBean
    private CounterDataValidator counterDataValidator;
    @Autowired
    private AdminRepository adminRepository;

    @Test
    void getAll() throws Exception {
        when(counterDataService.getPage(anyInt(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/counter-data/index/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/counter-data/get-all"))
                .andExpect(model().attributeExists("counters", "filter"));

        verify(counterDataService, times(1)).getPage(eq(1), any());
    }

    @Test
    void filter() throws Exception {
        CounterDataFilterDto filterDto = new CounterDataFilterDto();
        when(counterDataService.filter(any())).thenReturn(new ArrayList<>());
        when(counterDataService.getPage(eq(1), any(Model.class), anyList()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/counter-data/filter/{page}", 1)
                        .flashAttr("filter", filterDto))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/counter-data/get-all"))
                .andExpect(model().attributeExists("counters", "filter", "houses"));

        verify(counterDataService, times(1)).filter(eq(filterDto));
    }

    @Test
    void add() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/counter-data/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/counter-data/add"))
                .andExpect(model().attributeExists("statuses", "counterDataDTO"));
    }

    @Test
    void testAdd() throws Exception {
        CounterDataDTO counterDataDTO = new CounterDataDTO();
        counterDataDTO.setStatus(CounterDataStatus.New.name());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/counter-data/add")
                        .flashAttr("counterDataDTO", counterDataDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/counter-data/add"));
        counterDataDTO.setNumber("qewf");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/counter-data/add")
                        .flashAttr("counterDataDTO", counterDataDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/counter-data/index/1"));

        verify(counterDataService, times(1)).save(any());
    }

    @Test
    void copy() throws Exception {
        CounterDataDTO counterDataDTOForExample = new CounterDataDTO();

        when(counterDataMapper.toDto(any())).thenReturn(counterDataDTOForExample);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/counter-data/copy/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/counter-data/add"))
                .andExpect(model().attributeExists("statuses", "counterDataDTO"));

        verify(counterDataMapper, times(1)).toDto(any());
    }

    @Test
    void testCopy() throws Exception {
        CounterDataDTO counterDataDTO = new CounterDataDTO();
        counterDataDTO.setStatus(CounterDataStatus.New.name());

        when(counterDataService.getMaxId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/counter-data/copy")
                        .flashAttr("counterDataDTO", counterDataDTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("statuses"))
                .andExpect(view().name("/admin/counter-data/add"));

        counterDataDTO.setNumber("qwerty");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/counter-data/copy")
                        .flashAttr("counterDataDTO", counterDataDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/counter-data/copy/1"));

        verify(counterDataService, times(1)).save(any());
    }

    @Test
    void getById() throws Exception {
        CounterData counterData = new CounterData();
        counterData.setFlat(new Flat());
        List<CounterDataDTO> counterDataList = new ArrayList<>();

        when(counterDataService.getById(anyLong())).thenReturn(counterData);
        when(counterDataMapper.toDtoList(anyList())).thenReturn(counterDataList);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/counter-data/counter-list/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/counter-data/counter-list"))
                .andExpect(model().attributeExists("statuses", "filter", "counters", "id"));

        verify(counterDataService, times(1)).getById(eq(1L));
        verify(counterDataMapper, times(1)).toDtoList(eq(counterData.getFlat().getCounterData()));
    }

    @Test
    void testFilter() throws Exception {
        CounterDataFilterDto filterDto = new CounterDataFilterDto();
        filterDto.setHouse(1L);
        CounterData counterData = new CounterData();
        counterData.setFlat(new Flat());
        List<CounterDataDTO> counterDataList = new ArrayList<>();

        when(counterDataService.getById(anyLong())).thenReturn(counterData);
        when(counterDataService.filter(any(), anyString(), anyString(), any(), anyList())).thenReturn(counterDataList);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/counter-data/counter-list/filter/{id}", 1)
                        .param("number", "123")
                        .param("date", "1000-01-02")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/counter-data/counter-list"))
                .andExpect(model().attributeExists("statuses", "filter", "counters", "id"));

        verify(counterDataService, times(1)).getById(eq(1L));
    }

    @Test
    void editById() throws Exception {
        CounterDataDTO counterDataDTO = new CounterDataDTO();
        CounterData counterData = new CounterData();

        when(counterDataService.getById(anyLong())).thenReturn(counterData);
        when(counterDataMapper.toDto(any())).thenReturn(counterDataDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/counter-data/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/counter-data/add"))
                .andExpect(model().attributeExists("statuses", "counterDataDTO"));

        verify(counterDataService, times(1)).getById(eq(1L));
        verify(counterDataMapper, times(1)).toDto(eq(counterData));
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/counter-data/delete/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/counter-data/index/1"));

        verify(counterDataService, times(1)).deleteById(eq(1L));
    }

    @Test
    void index() throws Exception {
        CounterData counterData = new CounterData();
        AdditionalService additionalService = new AdditionalService();
        additionalService.setName("name");
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        unitOfMeasurement.setName("unit name");
        additionalService.setUnitOfMeasurement(unitOfMeasurement);
        counterData.setAdditionalService(additionalService);
        Flat flat = new Flat();
        House house = new House();
        flat.setHouse(house);
        Section section = new Section();
        section.setName("sec name");
        User user = new User();
        user.setId(1L);
        flat.setUser(user);
        flat.setSection(section);
        counterData.setFlat(flat);
        when(counterDataService.getById(anyLong())).thenReturn(counterData);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/counter-data/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/counter-data/index"))
                .andExpect(model().attributeExists("counter"));

        verify(counterDataService, times(1)).getById(eq(1L));
    }
}