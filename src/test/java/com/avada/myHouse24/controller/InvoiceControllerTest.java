package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.*;
import com.avada.myHouse24.mapper.InvoiceMapper;
import com.avada.myHouse24.model.InvoiceDto;
import com.avada.myHouse24.model.TemplateForInvoiceDTO;
import com.avada.myHouse24.repo.FlatRepository;
import com.avada.myHouse24.repo.HouseRepository;
import com.avada.myHouse24.repo.InvoiceRepository;
import com.avada.myHouse24.repo.TemplateForInvoiceRepository;
import com.avada.myHouse24.services.impl.*;
import com.avada.myHouse24.util.ImageUtil;
import com.avada.myHouse24.validator.InvoiceValidator;
import com.avada.myHouse24.validator.TemplateForInvoiceValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceServiceImpl invoiceService;

    @TempDir
    Path tempDir;

    @MockBean
    private AccountTransactionServiceImpl accountTransactionService;

    @MockBean
    private ScoreServiceImpl scoreService;

    @MockBean
    private InvoiceMapper invoiceMapper;

    @MockBean
    private TariffServiceImpl tariffService;
    @MockBean
    private TemplateForInvoiceServiceImpl templateForInvoiceService;
    @MockBean
    private TemplateForInvoiceValidator templateForInvoiceValidator;
    @MockBean
    private AdditionalServiceImpl additionalService;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private TemplateForInvoiceRepository templateForInvoiceRepository;

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getAll() throws Exception {
        when(invoiceService.getPage(anyInt(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(invoiceMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());
        when(accountTransactionService.getAllSum()).thenReturn(1.1);
        when(scoreService.getAllBalance()).thenReturn(1.1);
        when(accountTransactionService.getSumWhereIsIncomeIsFalse()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/index/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/get-all"))
                .andExpect(model().attributeExists("invoices", "filter", "sumAccountTransactionForStats", "balanceScoreForStats", "sumWhereIsIncomeIsFalse"));

        verify(invoiceService, times(1)).getPage(eq(1), any());
        verify(invoiceMapper, times(1)).toDtoList(anyList());
        verify(accountTransactionService, times(1)).getAllSum();
        verify(scoreService, times(1)).getAllBalance();
        verify(accountTransactionService, times(1)).getSumWhereIsIncomeIsFalse();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void filter() throws Exception {
        when(invoiceService.filter(any(), anyString(), any(Date.class))).thenReturn(new ArrayList<>());
        when(invoiceService.getPage(anyInt(), any(), any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(invoiceMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/filter/1")
                        .param("flatNumber", "123")
                        .param("dateExample", "2023-09-10"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/get-all"))
                .andExpect(model().attributeExists("invoices", "filter", "dateExample", "flatNumber"));

        verify(invoiceService, times(1)).filter(any(), eq("123"), eq(Date.valueOf("2023-09-10")));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void add() throws Exception {
        when(tariffService.getAll()).thenReturn(Collections.emptyList());
        when(additionalService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/add"))
                .andExpect(model().attributeExists("invoiceDto", "statuses", "tariffs", "services"));

        verify(tariffService, times(1)).getAll();
        verify(additionalService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testAdd() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/invoice/add")
                        .flashAttr("invoiceDto", new InvoiceDto())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/add"))
                .andExpect(model().attributeExists("invoiceDto", "statuses", "tariffs", "services"));

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(1L);
        invoiceDto.setNumber("1234");
        invoiceDto.setDate(new Date(1,1,2));
        invoiceDto.setToDate(new Date(1,1,2));
        invoiceDto.setFromDate(new Date(1,1,2));
        Flat flat = new Flat();
        flat.setId(1L);
        House house = new House();
        house.setId(1L);
        flat.setHouse(house);
        invoiceDto.setFlat(flat);
        invoiceDto.setUser(new User());
        Score score = new Score();
        score.setId(1L);
        invoiceDto.setScore(score);
        invoiceDto.setAddToStats(true);
        invoiceDto.setStatus("qewrty");
        invoiceDto.setMonths("month");
        Tariff tariff = new Tariff();
        tariff.setId(1L);
        invoiceDto.setTariff(tariff);
        invoiceDto.setSum(12.0);
        invoiceDto.setInvoiceAdditionalServices(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/invoice/add")
                        .flashAttr("invoiceDto", invoiceDto)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/invoice/index/1"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void deleteAllByCheckBox() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/deleteAllByCheckBox")
                        .param("selectedIntegers", "1", "2", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/invoice/index/1"));

        verify(invoiceService, times(1)).deleteById(1L);
        verify(invoiceService, times(1)).deleteById(2L);
        verify(invoiceService, times(1)).deleteById(3L);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/invoice/index/1"));

        verify(invoiceService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void edit() throws Exception {
        when(invoiceMapper.toDto(any())).thenReturn(new InvoiceDto());
        when(tariffService.getAll()).thenReturn(Collections.emptyList());
        when(additionalService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/add"))
                .andExpect(model().attributeExists("invoiceDto", "statuses", "tariffs", "services"));

        verify(invoiceMapper, times(1)).toDto(any());
        verify(tariffService, times(1)).getAll();
        verify(additionalService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void copy() throws Exception {
        when(invoiceMapper.toDto(any())).thenReturn(new InvoiceDto());
        when(tariffService.getAll()).thenReturn(Collections.emptyList());
        when(additionalService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/copy/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/add"))
                .andExpect(model().attributeExists("invoiceDto", "statuses", "tariffs", "services"));

        verify(invoiceMapper, times(1)).toDto(any());
        verify(tariffService, times(1)).getAll();
        verify(additionalService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getById() throws Exception {
        InvoiceDto invoiceDto = new InvoiceDto();
        Flat flat = new Flat();
        User user = new User();
        user.setId(1L);
        flat.setUser(user);
        user.setFirstName("firstName");
        invoiceDto.setUser(user);
        House house = new House();
        house.setId(1L);
        Section section = new Section();
        section.setName("adf");
        flat.setSection(section);
        Tariff tariff = new Tariff();
        tariff.setId(1L);
        invoiceDto.setTariff(tariff);
        flat.setHouse(house);
        invoiceDto.setSum(12.2);
        invoiceDto.setFlat(flat);
        when(invoiceMapper.toDto(any())).thenReturn(invoiceDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/index"))
                .andExpect(model().attributeExists("invoiceDto"));

        verify(invoiceMapper, times(1)).toDto(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void template() throws Exception {
        when(templateForInvoiceService.getAll()).thenReturn(Collections.emptyList());
        when(templateForInvoiceService.getTemplateWhereIsMainIsTrue()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/template"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/template"))
                .andExpect(model().attributeExists("templates"));

        verify(templateForInvoiceService, times(1)).getAll();
        verify(templateForInvoiceService, times(1)).getTemplateWhereIsMainIsTrue();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void templateAdd() throws Exception {
        when(templateForInvoiceValidator.supports(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/invoice/template/add")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/template"))
                .andExpect(model().attributeExists("templates"));

        TemplateForInvoiceDTO template = new TemplateForInvoiceDTO();
        template.setName("template name");
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/invoice/template/add")
                        .flashAttr("template", template)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/invoice/template"));
        verify(templateForInvoiceValidator, times(2)).validate(any(), any());
        verify(templateForInvoiceService, times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void download() throws Exception {
        Path tempFile = Files.createTempFile("mock", ".txt");

        String filePath = tempFile.toString();

        TemplateForInvoice templateForInvoice = new TemplateForInvoice();
        templateForInvoice.setFileName(filePath);

        when(templateForInvoiceService.getById(1L)).thenReturn(templateForInvoice);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/download/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void doDefault() throws Exception {

        when(templateForInvoiceService.getTemplateWhereIsMainIsTrue()).thenReturn(null);
        when(templateForInvoiceService.getById(anyLong())).thenReturn(new TemplateForInvoice());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/doDefault/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/invoice/template"));

        when(templateForInvoiceService.getTemplateWhereIsMainIsTrue()).thenReturn(new TemplateForInvoice());
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/doDefault/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/invoice/template"));
        verify(templateForInvoiceService, times(3)).getTemplateWhereIsMainIsTrue();
        verify(templateForInvoiceService, times(3)).save(any());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void remove() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/remove/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/invoice/template"));

        verify(templateForInvoiceService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void print() throws Exception {
        when(invoiceService.getById(anyLong())).thenReturn(new Invoice());
        when(templateForInvoiceService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/invoice/print/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/invoice/print"))
                .andExpect(model().attributeExists("invoice", "templates"));

        verify(invoiceService, times(1)).getById(1L);
        verify(templateForInvoiceService, times(1)).getAll();
    }
}