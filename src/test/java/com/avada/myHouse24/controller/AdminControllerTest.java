package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.enums.Theme;
import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.mapper.AdminMapper;
import com.avada.myHouse24.model.AdminForAddDTO;
import com.avada.myHouse24.model.AdminForViewDTO;
import com.avada.myHouse24.repo.AdminRepository;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleServiceImpl roleService;

    @MockBean
    private AdminServiceImpl adminService;
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private AdminMapper adminMapper;

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getAll() throws Exception {
        AdminForViewDTO adminForViewDTO = new AdminForViewDTO();
        adminForViewDTO.setFullName("full time");

        when(roleService.getAll()).thenReturn(Collections.emptyList());
        when(adminMapper.toDtoForView(any())).thenReturn(adminForViewDTO);
        when(adminService.getPage(eq(1), any(Model.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        when(adminService.getAuthAdmin()).thenReturn(new Admin());
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user-admin/index/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allStatus", "roles", "filter", "admins"));


        verify(roleService, times(1)).getAll();
        verify(adminService, times(1)).getPage(eq(1), any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void add() throws Exception {
        when(roleService.getAll()).thenReturn(Collections.emptyList());
        when(adminService.getAuthAdmin()).thenReturn(new Admin());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user-admin/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("roles", "statuses"))
                .andExpect(view().name("admin/user-admin/add"));

        verify(roleService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testAdd() throws Exception {
        AdminForAddDTO admin = new AdminForAddDTO();
        admin.setPassword("qwert");
        admin.setPasswordAgain("qwerty");
        when(adminService.getAuthAdmin()).thenReturn(new Admin());
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user-admin/create")
                        .with(csrf())
                        .flashAttr("admin", admin))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user-admin/add"));
        admin.setPassword("qwerty");
        admin.setFirstName("first name");
        admin.setLastName("last name");
        admin.setRole("role");
        admin.setPhone("09876");
        admin.setStatus(UserStatus.NEW);
        admin.setEmail("email@emai.com");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user-admin/create")
                        .with(csrf())
                        .flashAttr("admin", admin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user-admin/index/1"));

        admin.setPasswordAgain("qwer");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user-admin/create")
                        .with(csrf())
                        .flashAttr("admin", admin))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user-admin/add"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void index() throws Exception {
        AdminForViewDTO admin = new AdminForViewDTO();
        admin.setStatus(UserStatus.NEW);
        when(adminService.getById(anyLong())).thenReturn(new Admin());
        when(adminService.getAuthAdmin()).thenReturn(new Admin());
        when(adminMapper.toDtoForView(any())).thenReturn(admin);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user-admin/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("admin"))
                .andExpect(view().name("admin/user-admin/index"));

        verify(adminService, times(1)).getById(eq(1L));
        verify(adminMapper, times(1)).toDtoForView(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void delete() throws Exception {
        when(adminService.getById(anyLong())).thenReturn(new Admin());
        when(adminService.getAuthAdmin()).thenReturn(new Admin());
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user-admin/delete/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user-admin/index/1"));

        verify(adminService, times(1)).getById(eq(1L));
        verify(adminService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void edit() throws Exception {
        AdminForAddDTO admin = new AdminForAddDTO();
        admin.setRole("ROLE_ADMIN");

        when(adminService.getAuthAdmin()).thenReturn(new Admin());
        when(adminService.getById(anyLong())).thenReturn(new Admin());
        when(adminMapper.toDtoForAdd(any())).thenReturn(admin);
        when(roleService.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user-admin/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("admin", "roles", "statuses"))
                .andExpect(view().name("admin/user-admin/edit"));

        verify(adminService, times(1)).getById(eq(1L));
        verify(adminMapper, times(1)).toDtoForAdd(any());
        verify(roleService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testEdit() throws Exception {
        AdminForAddDTO admin = new AdminForAddDTO();
        admin.setId(1L);
        admin.setPassword("qwerty");
        admin.setPasswordAgain("werty");
        admin.setRole("ROLE_ADMIN");

        when(adminService.getById(anyLong())).thenReturn(new Admin());
        when(adminMapper.toDtoForAdd(any())).thenReturn(admin);
        when(adminService.getAuthAdmin()).thenReturn(new Admin());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user-admin/edit/{id}", 1)
                        .with(csrf())
                        .flashAttr("adminModel", admin))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user-admin/edit"));
        admin.setPasswordAgain("qwerty");
        admin.setFirstName("first name");
        admin.setLastName("last name");
        admin.setPhone("09876");
        admin.setStatus(UserStatus.NEW);
        admin.setEmail("email@emai.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user-admin/edit/{id}", 1)
                        .with(csrf())
                        .flashAttr("adminModel", admin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user-admin/index/1"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void filter() throws Exception {
        AdminForViewDTO adminForViewDto = new AdminForViewDTO();
        List<AdminForViewDTO> adminPage = Collections.singletonList(new AdminForViewDTO());
        when(adminService.getAuthAdmin()).thenReturn(new Admin());
        when(adminService.getAll()).thenReturn(Collections.singletonList(new Admin()));
        when(adminService.getPage(eq(1), any(Model.class), anyList()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        when(adminService.filter(any(), anyList())).thenReturn(adminPage);
        when(adminMapper.toDtoListForView(anyList())).thenReturn(Collections.singletonList(adminForViewDto));
        when(roleService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user-admin/filter/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allStatus", "roles", "filter", "admins"))
                .andExpect(view().name("admin/user-admin/get-all"));

        verify(adminService, times(1)).getAll();
        verify(adminService, times(1)).filter(any(), any());
        verify(adminMapper, times(1)).toDtoListForView(anyList());
        verify(roleService, times(1)).getAll();
    }
    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void changeTheme() throws Exception {
        Admin admin = new Admin();
        when(adminService.getAuthAdmin()).thenReturn(admin);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user-admin/change/theme/light"))
                .andExpect(status().isOk());
        assertEquals(Theme.LIGHT, admin.getTheme());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/user-admin/change/theme/dark"))
                .andExpect(status().isOk());
        assertEquals(Theme.DARK, admin.getTheme());

    }
}