package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.mapper.ScoreMapper;
import com.avada.myHouse24.model.ScoreDTO;
import com.avada.myHouse24.services.impl.*;
import com.avada.myHouse24.validator.ScoreValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ScoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HouseServiceImpl houseService;

    @MockBean
    private FlatServiceImpl flatService;

    @MockBean
    private FlatMapper flatMapper;

    @MockBean
    private ScoreValidator scoreValidator;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private ScoreServiceImpl scoreService;

    @MockBean
    private ScoreMapper scoreMapper;
    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getAll() throws Exception {
        when(houseService.getAll()).thenReturn(new ArrayList<>());
        when(flatService.getPage(anyInt(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(userService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/index/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/account/get-all"))
                .andExpect(model().attributeExists("houses", "sections", "users", "filter"));

        verify(scoreService, times(1)).getPage(eq(1), any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getSectionsByHouseId() throws Exception {
        House house = new House();
        house.setSections(new ArrayList<>());
        when(houseService.getById(anyLong())).thenReturn(house);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/getSections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getFlatsByHouseId() throws Exception {
        when(houseService.getById(anyLong())).thenReturn(new House());
        when(flatMapper.toDtoList(anyList())).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/getFlats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void add() throws Exception {
        when(houseService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/account/add"))
                .andExpect(model().attributeExists("houses", "sections", "flats"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testAdd() throws Exception {
        when(houseService.getAll()).thenReturn(new ArrayList<>());
        when(houseService.getById(anyLong())).thenReturn(new House());
        when(flatService.getById(anyLong())).thenReturn(new Flat());
        when(scoreMapper.toEntity(any())).thenReturn(new Score());
        when(scoreService.filter(any(), any())).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account/add")
                        .param("flat", "1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("houses"));

        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setNumber("some number");
        scoreDTO.setStatus("some status");
        scoreDTO.setHouse(new House());
        scoreDTO.setSection(new Section());
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account/add")
                        .flashAttr("scoreDto", scoreDTO)
                        .param("flat", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/account/index/1"));
        verify(scoreService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getById() throws Exception {
        Score score = new Score();
        Flat flat = new Flat();
        House house = new House();
        house.setId(1L);
        Section section = new Section();
        section.setName("section name");
        User user= new User();
        user.setId(1L);
        flat.setUser(user);
        flat.setHouse(house);
        flat.setSection(section);
        score.setBalance(12.2);
        score.setFlat(flat);
        when(scoreService.getById(anyLong())).thenReturn(score);
        flat.setCounterData(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/account/index"))
                .andExpect(model().attributeExists("scoreDto"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void edit() throws Exception {
        Score score = new Score();
        Flat flat = new Flat();
        flat.setId(1L);
        House house = new House();
        house.setId(1L);
        house.setFlats(Arrays.asList(flat));
        flat.setHouse(house);
        score.setFlat(flat);
        when(houseService.getAll()).thenReturn(new ArrayList<>());
        when(scoreService.getById(anyLong())).thenReturn(score);
        when(scoreMapper.toDto(any())).thenReturn(new ScoreDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/account/add"))
                .andExpect(model().attributeExists("houses", "scoreDto", "flats"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void filter() throws Exception {
        when(houseService.getAll()).thenReturn(new ArrayList<>());
        when(scoreService.filter(any(), any())).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/filter/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/account/get-all"))
                .andExpect(model().attributeExists("houses", "sections", "users", "filter"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/account/index/1"));

        verify(scoreService, times(1)).deleteById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getAllHouses() throws Exception {
        Score score = new Score();
        score.setId(1L);
        score.setNumber("number");
        when(scoreService.forSelect(anyInt(), anyInt(), anyString())).thenReturn(Arrays.asList(score));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/get-scores")
                        .param("_page", "1")
                        .param("_search", "someSearchTerm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.pagination").isMap());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getScoreByFlat() throws Exception {
        Flat flat = new Flat();
        flat.setScore(new Score());
        when(flatService.getById(anyLong())).thenReturn(flat);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/getScoreByFlat/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }
    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void excel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account/excel"))
                .andExpect(status().isOk());

        verify(scoreService, times(1)).excel(any());
    }
}