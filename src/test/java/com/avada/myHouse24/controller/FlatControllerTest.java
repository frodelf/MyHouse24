package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.model.FlatDTO;
import com.avada.myHouse24.services.impl.*;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.repository.util.ClassUtils.hasProperty;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FlatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlatServiceImpl flatService;

    @MockBean
    private HouseServiceImpl houseService;

    @MockBean
    private FloorServiceImpl floorService;

    @MockBean
    private ScoreServiceImpl scoreService;

    @MockBean
    private SectionServiceImpl sectionService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private TariffServiceImpl tariffService;

    @MockBean
    private FlatMapper flatMapper;
    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getAll() throws Exception {
        when(flatService.getPage(anyInt(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/index/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/flat/get-all"))
                .andExpect(model().attributeExists("flats", "filter", "houses", "users", "sections", "floors", "flatCount"));

        verify(flatService, times(1)).getPage(eq(1), any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void add() throws Exception {
        when(houseService.getAll()).thenReturn(Collections.emptyList());
        when(scoreService.getAllByStatus("Неактивен")).thenReturn(Collections.emptyList());
        when(userService.getAll()).thenReturn(Collections.emptyList());
        when(tariffService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/flat/add"))
                .andExpect(model().attributeExists("houses", "scores", "users", "tariffs"));

        verify(houseService, times(1)).getAll();
        verify(scoreService, times(1)).getAllByStatus("Неактивен");
        verify(userService, times(1)).getAll();
        verify(tariffService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getFloorsByHouseId() throws Exception {
        List<Floor> floors = Arrays.asList(new Floor(), new Floor());
        House house = new House();
        house.setFloors(Arrays.asList(new Floor()));
        when(houseService.getById(anyLong())).thenReturn(house);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/getFloors/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));

        verify(houseService, times(1)).getById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getSectionsByHouseId() throws Exception {
        House house = new House();
        List<Section> sections = Arrays.asList(new Section(), new Section());
        house.setSections(sections);
        when(houseService.getById(anyLong())).thenReturn(house);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/getSections/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(houseService, times(1)).getById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testAdd() throws Exception {
        FlatDTO flatDTO = new FlatDTO();
        flatDTO.setNumber(1234L);
        flatDTO.setArea(1234.0);
        flatDTO.setHouse(new House());
        flatDTO.setSection(new Section());
        flatDTO.setFloor(new Floor());
        flatDTO.setUser(new User());
        flatDTO.setTariff(new Tariff());
        flatDTO.setScoreNumber("543234");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/flat/add")
                        .flashAttr("flatDTO", flatDTO)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/flat/index/1"));
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/flat/add")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/flat/add"))
                .andExpect(model().attributeExists("houses", "scores", "users", "tariffs"));

        verify(flatService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void filter() throws Exception {
        List<FlatDTO> mockFlatDTOList = new ArrayList<>();
        mockFlatDTOList.add(new FlatDTO());
        when(flatMapper.toDtoList(anyList())).thenReturn(mockFlatDTOList);
        when(flatService.getPage(anyInt(), any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(flatService.filter(any(), anyList(), any())).thenReturn(Collections.emptyList());
        when(houseService.getAll()).thenReturn(Collections.emptyList());
        when(userService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/filter/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("flats", "filter", "houses", "users", "sections", "floors", "flatCount"));

        verify(flatService, times(1)).getPage(eq(1), any(), any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getById() throws Exception {
        FlatDTO flatDTO = new FlatDTO();
        flatDTO.setNumber(1345L);
        House house = new House();
        house.setName("qwetr");
        flatDTO.setArea(1234.0);
        flatDTO.setSection(new Section());
        flatDTO.setFloor(new Floor());
        flatDTO.setUser(new User());
        flatDTO.setTariff(new Tariff());
        flatDTO.setScoreNumber("543234");
        flatDTO.setHouse(house);

        when(flatService.getById(anyLong())).thenReturn(new Flat());
        when(flatMapper.toDto(any())).thenReturn(flatDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/{id}", 1))
                .andExpect(status().isOk());

        verify(flatService, times(2)).getById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void edit() throws Exception {

        when(flatService.getById(anyLong())).thenReturn(new Flat());
        when(houseService.getAll()).thenReturn(new ArrayList<>());
        when(floorService.getAll()).thenReturn(new ArrayList<>());
        when(sectionService.getAll()).thenReturn(new ArrayList<>());
        when(scoreService.getAllByStatus(anyString())).thenReturn(new ArrayList<>());
        when(userService.getAll()).thenReturn(new ArrayList<>());
        when(tariffService.getAll()).thenReturn(new ArrayList<>());

        FlatDTO flatDTO = new FlatDTO();
        flatDTO.setNumber(12344321L);
        House house = new House();
        house.setName("qwetr");
        flatDTO.setHouse(house);
        when(flatMapper.toDto(any())).thenReturn(flatDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/flat/edit"))
                .andExpect(model().attributeExists("flat", "houses", "floors", "sections", "scores", "users", "tariffs"));

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void deleteById() throws Exception {
        Flat flat = new Flat();
        flat.setScore(new Score());
        when(flatService.getById(anyLong())).thenReturn(flat);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/delete/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/flat/index/1"));

        verify(scoreService, times(1)).save(any(Score.class));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void copy() throws Exception {
        when(flatService.getById(anyLong())).thenReturn(new Flat());
        when(houseService.getAll()).thenReturn(new ArrayList<>());
        when(floorService.getAll()).thenReturn(new ArrayList<>());
        when(sectionService.getAll()).thenReturn(new ArrayList<>());
        when(scoreService.getAllByStatus(anyString())).thenReturn(new ArrayList<>());
        when(userService.getAll()).thenReturn(new ArrayList<>());
        when(tariffService.getAll()).thenReturn(new ArrayList<>());

        FlatDTO flatDTO = new FlatDTO();
        flatDTO.setNumber(12344321L);
        House house = new House();
        house.setName("qwetr");
        flatDTO.setHouse(house);
        when(flatMapper.toDto(any())).thenReturn(flatDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/copy/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/flat/copy"))
                .andExpect(model().attributeExists("flat", "houses", "floors", "sections", "scores", "users", "tariffs"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testCopy() throws Exception {
        when(scoreService.existNumber(anyString())).thenReturn(false);
        when(flatService.getMaxId()).thenReturn(2);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/flat/copy")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/flat/add"))
                .andExpect(model().attributeExists("houses", "scores", "users", "tariffs"));
        FlatDTO flatDTO = new FlatDTO();
        flatDTO.setNumber(12L);
        flatDTO.setArea(12.1);
        flatDTO.setHouse(new House());
        flatDTO.setSection(new Section());
        flatDTO.setFloor(new Floor());
        flatDTO.setUser(new User());
        flatDTO.setTariff(new Tariff());
        flatDTO.setScoreNumber("3456789");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/flat/copy")
                        .flashAttr("flatDTO", flatDTO)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/flat/copy/1"));

        verify(flatService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getByName() throws Exception {
        Flat flat = new Flat();
        flat.setId(1L);
        when(flatService.getByNumber(anyInt())).thenReturn(flat);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/name/{name}", 123))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/flat/1"));

        verify(flatService, times(1)).getByNumber(eq(123));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getFlatByScore() throws Exception {
        Score score = new Score();
        Flat flat = new Flat();
        flat.setId(1L);
        score.setFlat(flat);
        when(scoreService.getById(anyLong())).thenReturn(score);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/getFlatByScore/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(scoreService, times(1)).getById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getFlatsByFlat() throws Exception {
        Flat flat = new Flat();
        House house = new House();
        house.setFlats(new ArrayList<>());
        flat.setHouse(house);
        when(flatService.getById(anyLong())).thenReturn(flat);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/getFlatsByFlat/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getSectionsByFlat() throws Exception {
        Flat flat = new Flat();
        House house = new House();
        house.setSections(new ArrayList<>());
        flat.setHouse(house);
        when(flatService.getById(anyLong())).thenReturn(flat);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/flat/getSectionsByFlat/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

    }
}