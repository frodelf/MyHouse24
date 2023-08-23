package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.mapper.AccountTransactionMapper;
import com.avada.myHouse24.model.AccountTransactionForViewDTO;
import com.avada.myHouse24.model.AccountTransactionInDTO;
import com.avada.myHouse24.model.AccountTransactionOutDTO;
import com.avada.myHouse24.services.impl.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountTransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountTransactionMapper accountTransactionMapper;

    @MockBean
    private AccountTransactionServiceImpl accountTransactionService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private AdminServiceImpl adminService;

    @MockBean
    private ScoreServiceImpl scoreService;

    @MockBean
    private TransactionPurposeServiceImpl transactionPurposeService;
    @Mock
    private ModelAndView modelAndView;
    @Mock
    private BindingResult bindingResult;
    @Test
    void index() throws Exception {
        AccountTransactionForViewDTO accountTransactionForViewDTO = new AccountTransactionForViewDTO();
        accountTransactionForViewDTO.setIsIncome(true);
        when(accountTransactionMapper.toDtoForViewList(anyList())).thenReturn(Collections.singletonList(accountTransactionForViewDTO));
        when(accountTransactionService.getSumWhereIsIncomeIsTrue()).thenReturn(8L);
        when(accountTransactionService.getSumWhereIsIncomeIsFalse()).thenReturn(10L);
        when(accountTransactionService.getPage(eq(1), any(ModelAndView.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));


        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/index/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/account-transaction/get-all"))
                .andExpect(model().attributeExists(
                        "accountTransaction", "accountTransactionList",
                        "transactionPurposeList", "sumWhereIsIncomeIsTrue",
                        "sumWhereIsIncomeIsFalse"
                ));

        verify(accountTransactionMapper, times(1)).toDtoForViewList(anyList());
        verify(accountTransactionService, times(1)).getSumWhereIsIncomeIsTrue();
        verify(accountTransactionService, times(1)).getSumWhereIsIncomeIsFalse();
        verify(accountTransactionService, times(1)).getPage(eq(1), any(ModelAndView.class));
    }

    @Test
    void addIn() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptyList());
        when(adminService.getAll()).thenReturn(Collections.emptyList());
        when(scoreService.getAll()).thenReturn(Collections.emptyList());
        when(transactionPurposeService.getAllIncomeTrue()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/create/in"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/account-transaction/add-in"))
                .andExpect(model().attributeExists(
                        "users", "admins", "scores",
                        "transactionPurposes", "maxId", "fromDate"
                ));

        verify(userService, times(1)).getAll();
        verify(adminService, times(1)).getAll();
        verify(scoreService, times(1)).getAll();
        verify(transactionPurposeService, times(1)).getAllIncomeTrue();
    }

    @Test
    void testAddIn() throws Exception {
        AccountTransactionInDTO dto = new AccountTransactionInDTO(1L, new Date(1,1,1), "transactionPurposeName",
                "userName","scoreId",true,"10.0","adminName","comment",true,"number");
        when(accountTransactionMapper.toEntityForIn(any())).thenReturn(new AccountTransaction());
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account-transaction/create/in"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(
                        "users","admins","scores",
                        "transactionPurposes","maxId","fromDate"
                ));
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account-transaction/create/in")
                        .flashAttr("accountTransactionInDTO", dto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/account-transaction/index/1"));

        verify(accountTransactionMapper, times(1)).toEntityForIn(any());
        verify(accountTransactionService, times(1)).save(any());
    }

    @Test
    void updateIn() throws Exception {
        AccountTransactionInDTO dummyDto = new AccountTransactionInDTO();
        when(accountTransactionMapper.toDtoForIn(any())).thenReturn(dummyDto);
        when(userService.getAll()).thenReturn(Collections.emptyList());
        when(adminService.getAll()).thenReturn(Collections.emptyList());
        when(scoreService.getAll()).thenReturn(Collections.emptyList());
        when(transactionPurposeService.getAllIncomeTrue()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/update/in/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/account-transaction/update-in"))
                .andExpect(model().attributeExists(
                        "accountTransaction", "users", "admins", "scores", "transactionPurposes"
                ));

        verify(accountTransactionMapper, times(1)).toDtoForIn(any());
        verify(userService, times(1)).getAll();
        verify(adminService, times(1)).getAll();
        verify(scoreService, times(1)).getAll();
        verify(transactionPurposeService, times(1)).getAllIncomeTrue();
    }

    @Test
    void testUpdateIn() throws Exception {
        AccountTransactionInDTO dto = new AccountTransactionInDTO(1L, new Date(1,1,1), "transactionPurposeName",
                "userName","scoreId",true,"10.0","adminName","comment",true,"number");

        when(accountTransactionMapper.toEntityForIn(any())).thenReturn(new AccountTransaction());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account-transaction/update/in/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("accountTransaction","users","admins","scores","transactionPurposes"));
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account-transaction/update/in/1")
                        .flashAttr("accountTransactionInDTO", dto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/account-transaction/index/1"));

        verify(accountTransactionMapper, times(1)).toEntityForIn(any());
        verify(accountTransactionService, times(1)).save(any());
    }

    @Test
    void addOut() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptyList());
        when(adminService.getAll()).thenReturn(Collections.emptyList());
        when(scoreService.getAll()).thenReturn(Collections.emptyList());
        when(transactionPurposeService.getAllIncomeFalse()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/create/out"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/account-transaction/add-out"))
                .andExpect(model().attributeExists(
                        "users", "admins", "scores", "transactionPurposes", "maxId", "fromDate"
                ));

        verify(userService, times(1)).getAll();
        verify(adminService, times(1)).getAll();
        verify(scoreService, times(1)).getAll();
        verify(transactionPurposeService, times(1)).getAllIncomeFalse();
    }

    @Test
    void testAddOut() throws Exception {
        AccountTransactionOutDTO accountTransactionOutDTO = new AccountTransactionOutDTO(1L, new Date(1,1,1), "transactionPurposeName",
                true, "10.0", "adminName","comment","number",true);
        when(accountTransactionMapper.toEntityForOut(any())).thenReturn(new AccountTransaction());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account-transaction/create/out"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users","admins","scores","transactionPurposes","maxId","fromDate"));
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account-transaction/create/out")
                        .flashAttr("accountTransactionOutDTO", accountTransactionOutDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/account-transaction/index/1"));

        verify(accountTransactionMapper, times(1)).toEntityForOut(any());
        verify(accountTransactionService, times(1)).save(any());
    }

    @Test
    void updateOut() throws Exception {
        AccountTransactionOutDTO dummyDto = new AccountTransactionOutDTO();
        when(accountTransactionMapper.toDtoForOut(any())).thenReturn(dummyDto);
        when(adminService.getAll()).thenReturn(Collections.emptyList());
        when(transactionPurposeService.getAllIncomeFalse()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/update/out/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/account-transaction/update-out"))
                .andExpect(model().attributeExists(
                        "accountTransaction", "admins", "transactionPurposes", "fromDate"
                ));

        verify(accountTransactionMapper, times(1)).toDtoForOut(any());
        verify(adminService, times(1)).getAll();
        verify(transactionPurposeService, times(1)).getAllIncomeFalse();
    }

    @Test
    void testUpdateOut() throws Exception {
        AccountTransactionOutDTO accountTransactionOutDTO = new AccountTransactionOutDTO(1L, new Date(1,1,1), "transactionPurposeName",
                true, "10.0", "adminName","comment","number",true);
        when(accountTransactionMapper.toEntityForOut(any())).thenReturn(new AccountTransaction());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account-transaction/update/out/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("accountTransaction","admins","transactionPurposes"));
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/account-transaction/update/out/1")
                        .flashAttr("accountTransactionOutDTO", accountTransactionOutDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/account-transaction/index/1"));

        verify(accountTransactionMapper, times(1)).toEntityForOut(any());
        verify(accountTransactionService, times(1)).save(any());
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/account-transaction/index/1"));

        verify(accountTransactionService, times(1)).deleteById(1L);
    }

    @Test
    void getAccountTransaction() throws Exception {
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setId(1L);
        accountTransaction.setIncome(true);
        AccountTransactionForViewDTO accountTransactionForViewDTO = new AccountTransactionForViewDTO();
        accountTransactionForViewDTO.setId(1L);
        accountTransactionForViewDTO.setIsIncome(true);
        when(accountTransactionMapper.toDtoForView(any())).thenReturn(accountTransactionForViewDTO);
        when(accountTransactionService.getById(anyLong())).thenReturn(accountTransaction);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/account-transaction/index"))
                .andExpect(model().attributeExists("accountTransaction"));

        verify(accountTransactionMapper, times(1)).toDtoForView(any());
        verify(accountTransactionService, times(1)).getById(anyLong());
    }

    @Test
    void copyIn() throws Exception {
        AccountTransaction dummyTransaction = new AccountTransaction();
        when(accountTransactionService.getById(anyLong())).thenReturn(dummyTransaction);
        when(accountTransactionService.getMaxId()).thenReturn(1L);
        when(accountTransactionMapper.toDtoForIn(any())).thenReturn(new AccountTransactionInDTO());
        when(userService.getAll()).thenReturn(Collections.emptyList());
        when(adminService.getAll()).thenReturn(Collections.emptyList());
        when(scoreService.getAll()).thenReturn(Collections.emptyList());
        when(transactionPurposeService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/copy/in/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/account-transaction/update-in"))
                .andExpect(model().attributeExists(
                        "accountTransaction", "users", "admins", "scores", "transactionPurposes"
                ));

        verify(accountTransactionService, times(1)).getById(anyLong());
        verify(accountTransactionService, times(1)).getMaxId();
        verify(accountTransactionMapper, times(1)).toDtoForIn(any());
        verify(userService, times(1)).getAll();
        verify(adminService, times(1)).getAll();
        verify(scoreService, times(1)).getAll();
        verify(transactionPurposeService, times(1)).getAll();
    }

    @Test
    void copyOut() throws Exception {
        AccountTransaction accountTransaction = new AccountTransaction();
        when(accountTransactionService.getById(anyLong())).thenReturn(accountTransaction);
        when(accountTransactionService.getMaxId()).thenReturn(1L);
        when(accountTransactionMapper.toDtoForOut(any())).thenReturn(new AccountTransactionOutDTO());
        when(adminService.getAll()).thenReturn(Collections.emptyList());
        when(transactionPurposeService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/copy/out/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(
                        "accountTransaction", "admins", "transactionPurposes", "maxId", "fromDate"
                ));

        verify(accountTransactionService, times(1)).getById(anyLong());
        verify(accountTransactionService, times(1)).getMaxId();
        verify(accountTransactionMapper, times(1)).toDtoForOut(any());
        verify(transactionPurposeService, times(1)).getAll();
    }

    @Test
    void filter() throws Exception {
        AccountTransactionForViewDTO accountTransactionForViewDTO = new AccountTransactionForViewDTO();
        when(accountTransactionService.getPage(eq(1), any(ModelAndView.class), anyList()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        when(transactionPurposeService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/filter/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/account-transaction/get-all"))
                .andExpect(model().attributeExists(
                        "accountTransaction", "accountTransactionList", "transactionPurposeList",
                        "sumWhereIsIncomeIsTrue", "sumWhereIsIncomeIsFalse"
                ));

        verify(accountTransactionService, times(1)).filter(any(), anyList());
        verify(transactionPurposeService, times(1)).getAll();
    }

    @Test
    void excel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/account-transaction/excel"))
                .andExpect(status().isOk());

        verify(accountTransactionService, times(1)).excel(any());
    }
}