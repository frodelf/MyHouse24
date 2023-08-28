package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.entity.Admin;
import com.avada.myHouse24.enums.UserStatus;
import com.avada.myHouse24.model.AdminForViewDTO;
import com.avada.myHouse24.repo.AdminRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceImplTest {
    @InjectMocks
    private AdminServiceImpl adminService;
    @Mock
    private AdminRepository adminRepository;
    @Test
    void getByName() {
        String adminName = "John";
        Admin mockAdmin = new Admin();
        mockAdmin.setFirstName(adminName);
        when(adminRepository.findByFirstName(adminName)).thenReturn(Optional.of(mockAdmin));

        Admin result = adminService.getByName(adminName);

        assertEquals(mockAdmin, result);
        verify(adminRepository, times(1)).findByFirstName(adminName);
    }

    @Test
    void getById() {
        Long adminId = 1L;
        Admin mockAdmin = new Admin();
        mockAdmin.setId(adminId);
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(mockAdmin));

        Admin result = adminService.getById(adminId);

        assertEquals(mockAdmin, result);
        verify(adminRepository, times(1)).findById(adminId);
    }

    @Test
    void getAll() {
        List<Admin> mockAdmins = new ArrayList<>();
        when(adminRepository.findAll()).thenReturn(mockAdmins);

        List<Admin> result = adminService.getAll();

        assertEquals(mockAdmins, result);
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    void save() {
        Admin mockAdmin = new Admin();
        String password = "password";
        mockAdmin.setPassword(password);
        adminService.save(mockAdmin);

        verify(adminRepository, times(1)).save(mockAdmin);
        assertNotEquals(password, mockAdmin.getPassword()); // Ensure that the password was encoded
    }

    @Test
    void getPage() {
        List<Admin> mockAdmins = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockAdmins.add(new Admin());
        }
        when(adminRepository.findAll()).thenReturn(mockAdmins);

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockAdmins.size());

        List<Admin> currentPageItems = mockAdmins.subList(startIndex, endIndex);

        Page<Admin> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockAdmins.size());

        Model mockModel = new ExtendedModelMap();
        when(adminRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);


        Page<Admin> result = adminService.getPage(0, mockModel);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
        verify(adminRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void getOnlyName() {
        List<Admin> mockAdmins = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Admin admin = new Admin();
            admin.setFirstName("Admin" + i);
            mockAdmins.add(admin);
        }
        when(adminRepository.findAll()).thenReturn(mockAdmins);

        List<String> result = adminService.getOnlyName();

        assertEquals(5, result.size());
        for (int i = 0; i < 5; i++) {
            assertEquals("Admin" + i, result.get(i));
        }
    }

    @Test
    void getPageWithDTOList() {
        List<AdminForViewDTO> mockAdmins = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockAdmins.add(new AdminForViewDTO());
        }

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockAdmins.size());

        List<AdminForViewDTO> currentPageItems = mockAdmins.subList(startIndex, endIndex);

        Page<AdminForViewDTO> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockAdmins.size());

        Model mockModel = new ExtendedModelMap();


        Page<AdminForViewDTO> result = adminService.getPage(0, mockModel, mockAdmins);

        assertEquals(mockPage, result);
        assertEquals(1, mockModel.getAttribute("max"));
        assertEquals(1, mockModel.getAttribute("current"));
    }

    @Test
    void filter() {
        AdminForViewDTO filterDTO = new AdminForViewDTO();
        filterDTO.setFullName("2");
        filterDTO.setRole("2");
        filterDTO.setPhone("2");
        filterDTO.setEmail("2");
        filterDTO.setStatus(UserStatus.NEW);

        List<AdminForViewDTO> mockDTOList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AdminForViewDTO dto = new AdminForViewDTO();
            dto.setFullName("Admin" + i);
            dto.setRole("Role" + i);
            dto.setPhone("12345" + i);
            dto.setEmail("admin" + i + "@example.com");
            dto.setStatus(UserStatus.NEW);
            mockDTOList.add(dto);
        }

        List<AdminForViewDTO> result = adminService.filter(filterDTO, mockDTOList);

        assertEquals(1, result.size());
        assertEquals("Admin2", result.get(0).getFullName());
        assertEquals("Role2", result.get(0).getRole());
        assertEquals("123452", result.get(0).getPhone());
        assertEquals("admin2@example.com", result.get(0).getEmail());
        assertEquals(result.get(0).getStatus(), UserStatus.NEW);
    }

    @Test
    void getAuthAdmin() {
        Admin mockAdmin = new Admin();
        when(adminRepository.findById(1L)).thenReturn(Optional.of(mockAdmin));

        Admin result = adminService.getAuthAdmin();

        assertEquals(mockAdmin, result);
        verify(adminRepository, times(1)).findById(1L);
    }
}