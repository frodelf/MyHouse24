package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.mapper.HouseMapper;
import com.avada.myHouse24.model.HouseForAddDto;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.FlatServiceImpl;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.UserServiceImpl;
import com.avada.myHouse24.validator.HouseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HouseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HouseServiceImpl houseService;

    @MockBean
    private HouseValidator houseValidator;

    @MockBean
    private FlatServiceImpl flatService;

    @MockBean
    private AdminServiceImpl adminService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private HouseMapper houseMapper;
    @BeforeEach
    void setUp() {
        when(adminService.getAuthAdmin()).thenReturn(new Admin());
    }
    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getAll() throws Exception {
        when(houseService.getPage(anyInt(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(houseMapper.toDtoForViewList(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/index/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/house/get-all"))
                .andExpect(model().attributeExists("houses", "filter", "housesCount"));

        verify(houseService, times(1)).getPage(eq(0), any());
        verify(houseMapper, times(1)).toDtoForViewList(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void add() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/house/add"))
                .andExpect(model().attributeExists("house", "users"));

        verify(adminService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testAdd() throws Exception {
        when(houseValidator.supports(any())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/house/add")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/house/add"))
                .andExpect(model().attributeExists("users"));
        HouseForAddDto house = new HouseForAddDto();
        house.setName("house name");
        house.setAddress("house address");
        house.setImage(new MockMultipartFile("1", new byte[1]));
        house.setImage1(new MockMultipartFile("1", new byte[1]));
        house.setImage2(new MockMultipartFile("1", new byte[1]));
        house.setImage3(new MockMultipartFile("1", new byte[1]));
        house.setImage4(new MockMultipartFile("1", new byte[1]));
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/house/add")
                        .flashAttr("house", house)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/house/index/1"));
        verify(houseValidator, times(2)).validate(any(), any());
        verify(houseService, times(1)).add(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void filter() throws Exception {
        when(houseService.filter(any(), any())).thenReturn(new ArrayList<>());
        when(houseService.getPage(anyInt(), any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(houseMapper.toDtoForViewList(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/filter/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/house/get-all"))
                .andExpect(model().attributeExists("houses", "filter", "housesCount"));

        verify(houseService, times(1)).filter(any(), any());
        verify(houseMapper, times(1)).toDtoForViewList(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/house/index/1"));

        verify(houseService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void index() throws Exception {
        House house = new House();
        house.setSections(new ArrayList<>());
        house.setFloors(new ArrayList<>());
        house.setAdmins(new ArrayList<>());
        when(houseService.getById(anyLong())).thenReturn(house);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/house/index"))
                .andExpect(model().attributeExists("house"));

        verify(houseService, times(1)).getById(1L);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getByName() throws Exception {
        House house = new House();
        house.setId(1L);
        when(houseService.getByName(anyString())).thenReturn(house);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/name/TestHouse"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/house/1"));

        verify(houseService, times(1)).getByName("TestHouse");
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void edit() throws Exception {
        House house = new House();
        house.setSections(new ArrayList<>());
        house.setFloors(new ArrayList<>());
        house.setAdmins(new ArrayList<>());
        when(houseService.getById(anyLong())).thenReturn(house);
        when(adminService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/house/edit"))
                .andExpect(model().attributeExists("house", "users"));

        verify(houseService, times(1)).getById(1L);
        verify(adminService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testEdit() throws Exception {
        when(adminService.getAll()).thenReturn(new ArrayList<>());
        House house = new House();
        house.setId(1L);
        house.setSections(new ArrayList<>());
        house.setFloors(new ArrayList<>());
        house.setAdmins(new ArrayList<>());
        when(houseService.getById(anyLong())).thenReturn(house);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/house/edit/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/house/edit"))
                .andExpect(model().attributeExists("users", "house"));
        HouseForAddDto houseForAdd = new HouseForAddDto();
        houseForAdd.setName("house name");
        houseForAdd.setAddress("house address");
        houseForAdd.setImage(new MockMultipartFile("1", new byte[1]));
        houseForAdd.setImage1(new MockMultipartFile("1", new byte[1]));
        houseForAdd.setImage2(new MockMultipartFile("1", new byte[1]));
        houseForAdd.setImage3(new MockMultipartFile("1", new byte[1]));
        houseForAdd.setImage4(new MockMultipartFile("1", new byte[1]));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/house/edit/1")
                        .flashAttr("house", houseForAdd)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/house/index/1"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getAllHouses() throws Exception {
        House house = new House();
        house.setId(1L);
        house.setName("name");
        when(houseService.forSelect(anyInt(), anyInt(), anyString())).thenReturn(Arrays.asList(house));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/get-houses")
                        .param("_page", "1")
                        .param("_search", "searchQuery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));

        verify(houseService, times(1)).forSelect(1, 10, "searchQuery");
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getHouseById() throws Exception {
        Flat flat = new Flat();
        House house = new House();
        house.setId(1L);
        flat.setHouse(house);
        when(flatService.getById(anyLong())).thenReturn(flat);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/getHouseByFlat/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(flatService, times(1)).getById(1L);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getPage() throws Exception {
        when(houseService.getPage(anyInt(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(houseMapper.toDtoForViewList(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/house/getAll/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(houseMapper, times(1)).toDtoForViewList(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void filterPage() throws Exception {
        when(houseService.filter(any(), any())).thenReturn(new ArrayList<>());
        when(houseService.getPage(anyInt(), any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(houseMapper.toDtoForViewList(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/house/filterGet/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(houseService, times(2)).filter(any(), any());
        verify(houseMapper, times(1)).toDtoForViewList(any());
    }
}