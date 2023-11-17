package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.TransactionPurpose;
import com.avada.myHouse24.mapper.TransactionPurposeMapper;
import com.avada.myHouse24.model.TransactionPurposeDTO;
import com.avada.myHouse24.services.impl.TransactionPurposeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionPurposeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionPurposeServiceImpl transactionPurposeService;

    @MockBean
    private TransactionPurposeMapper transactionPurposeMapper;
    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void index() throws Exception {
        when(transactionPurposeService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/transaction-purpose/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/transaction-purpose/get-all"))
                .andExpect(model().attributeExists("purposes"));

        verify(transactionPurposeService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void add() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/transaction-purpose/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/transaction-purpose/add"))
                .andExpect(model().attributeExists("transactionPurposeDTO"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testAdd() throws Exception {
        when(transactionPurposeMapper.toEntity(any())).thenReturn(new TransactionPurpose());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/transaction-purpose/create")
                        .with(csrf()))
                .andExpect(status().isOk());

        TransactionPurposeDTO transactionPurposeDTO = new TransactionPurposeDTO();
        transactionPurposeDTO.setName("transaction name");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/transaction-purpose/create")
                        .flashAttr("transactionPurposeDTO", transactionPurposeDTO)
                        .with(csrf()))
                .andExpect(redirectedUrl("/admin/transaction-purpose/index"))
                .andExpect(status().is3xxRedirection());
        verify(transactionPurposeService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void edit() throws Exception {
        when(transactionPurposeService.getById(anyLong())).thenReturn(new TransactionPurpose());
        TransactionPurposeDTO transactionPurposeDTO = new TransactionPurposeDTO();
        transactionPurposeDTO.setIsIncome(true);
        when(transactionPurposeMapper.toDto(any())).thenReturn(transactionPurposeDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/transaction-purpose/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/transaction-purpose/edit"))
                .andExpect(model().attributeExists("purpose"));

        verify(transactionPurposeService, times(1)).getById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testEdit() throws Exception {
        when(transactionPurposeMapper.toEntity(any())).thenReturn(new TransactionPurpose());
        when(transactionPurposeService.getById(anyLong())).thenReturn(new TransactionPurpose());
        TransactionPurposeDTO transactionPurposeDTO = new TransactionPurposeDTO();
        transactionPurposeDTO.setIsIncome(true);
        transactionPurposeDTO.setName("transaction name");
        transactionPurposeDTO.setId(1L);
        when(transactionPurposeMapper.toDto(any())).thenReturn(transactionPurposeDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/transaction-purpose/edit/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("purpose"));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/transaction-purpose/edit/1")
                        .flashAttr("transactionPurposeDTO", transactionPurposeDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/transaction-purpose/index"));
        verify(transactionPurposeService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/transaction-purpose/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/transaction-purpose/index"));

        verify(transactionPurposeService, times(1)).deleteById(anyLong());
    }
}