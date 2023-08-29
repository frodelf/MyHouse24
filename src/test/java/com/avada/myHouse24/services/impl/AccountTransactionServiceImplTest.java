package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.entity.TransactionPurpose;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.model.AccountTransactionForViewDTO;
import com.avada.myHouse24.repo.AccountTransactionRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountTransactionServiceImplTest {
    @InjectMocks
    private AccountTransactionServiceImpl accountTransactionService;

    @Mock
    private AccountTransactionRepository accountTransactionRepository;
    @Test
    void getAll() {
        List<AccountTransaction> mockTransactions = new ArrayList<>();
        when(accountTransactionRepository.findAll()).thenReturn(mockTransactions);

        List<AccountTransaction> result = accountTransactionService.getAll();

        assertEquals(mockTransactions, result);
        verify(accountTransactionRepository, times(1)).findAll();
    }

    @Test
    void getMaxIdWithoutError() {
        when(accountTransactionRepository.findMaxId()).thenReturn(5L);

        long result = accountTransactionService.getMaxId();

        assertEquals(6L, result);
        verify(accountTransactionRepository, times(1)).findMaxId();
    }
    @Test
    void getMaxIdWithError() {
        when(accountTransactionRepository.findMaxId()).thenThrow(new RuntimeException("Помилка під час отримання максимального ID"));;

        long result = accountTransactionService.getMaxId();

        verify(accountTransactionRepository, times(1)).findMaxId();
    }

    @Test
    void getSumWhereIsIncomeIsTrue() {
        when(accountTransactionRepository.sumWhereIsIncomeIsTrue()).thenReturn(100L);

        long result = accountTransactionService.getSumWhereIsIncomeIsTrue();

        assertEquals(100L, result);
        verify(accountTransactionRepository, times(1)).sumWhereIsIncomeIsTrue();
    }

    @Test
    void getSumWhereIsIncomeIsFalse() {
        when(accountTransactionRepository.sumWhereIsIncomeIsFalse()).thenReturn(50L);

        long result = accountTransactionService.getSumWhereIsIncomeIsFalse();

        assertEquals(50L, result);
        verify(accountTransactionRepository, times(1)).sumWhereIsIncomeIsFalse();
    }

    @Test
    void save() {
        AccountTransaction mockTransaction = new AccountTransaction();
        accountTransactionService.save(mockTransaction);
        verify(accountTransactionRepository, times(1)).save(mockTransaction);
    }

    @Test
    void getById() {
        AccountTransaction mockTransaction = new AccountTransaction();
        mockTransaction.setId(1L);
        when(accountTransactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));

        AccountTransaction result = accountTransactionService.getById(1L);

        assertEquals(mockTransaction, result);
        verify(accountTransactionRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById() {
        accountTransactionService.deleteById(1L);
        verify(accountTransactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void getByNumberWithoutError() {
        String transactionNumber = "123456789";
        AccountTransaction mockTransaction = new AccountTransaction();
        mockTransaction.setNumber(transactionNumber);
        List<AccountTransaction> mockTransactions = Collections.singletonList(mockTransaction);
        when(accountTransactionRepository.findAll()).thenReturn(mockTransactions);

        AccountTransaction result = accountTransactionService.getByNumber(transactionNumber);

        assertEquals(mockTransaction, result);
        verify(accountTransactionRepository, times(1)).findAll();
    }
    @Test
    void getByNumberWithError() {
        String transactionNumber = "123456789";
        AccountTransaction mockTransaction = new AccountTransaction();
        mockTransaction.setNumber(transactionNumber);
        List<AccountTransaction> mockTransactions = Collections.singletonList(mockTransaction);
        when(accountTransactionRepository.findAll()).thenReturn(mockTransactions);

        assertThrows(RuntimeException.class, () -> {
            accountTransactionService.getByNumber("1234567");
        });

    }
    @Test
    void getPage() {
        List<AccountTransaction> mockTransactions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockTransactions.add(new AccountTransaction());
        }
        when(accountTransactionRepository.findAll()).thenReturn(mockTransactions);

        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockTransactions.size());

        List<AccountTransaction> currentPageItems = mockTransactions.subList(startIndex, endIndex);

        Page<AccountTransaction> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockTransactions.size());

        ModelAndView mockModelAndView = new ModelAndView();
        when(accountTransactionRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        Page<AccountTransaction> result = accountTransactionService.getPage(0, mockModelAndView);

        assertEquals(mockPage, result);
        assertEquals(1, mockModelAndView.getModel().get("max"));
        assertEquals(1, mockModelAndView.getModel().get("current"));
        verify(accountTransactionRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetPage() {
        List<AccountTransactionForViewDTO> mockTransactions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockTransactions.add(new AccountTransactionForViewDTO());
        }
        List<AccountTransaction> accountTransactionList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            accountTransactionList.add(new AccountTransaction());
        }
        ModelAndView mockModelAndView = new ModelAndView();
        int page = 0;
        int pageSize = 10;

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mockTransactions.size());

        List<AccountTransactionForViewDTO> currentPageItems = mockTransactions.subList(startIndex, endIndex);

        Page<AccountTransactionForViewDTO> mockPage = new PageImpl<>(currentPageItems, PageRequest.of(page, pageSize), mockTransactions.size());

        when(accountTransactionRepository.findAll()).thenReturn(accountTransactionList);

        Page<AccountTransactionForViewDTO> result = accountTransactionService.getPage(0, mockModelAndView, mockTransactions);

        assertEquals(mockPage, result);
        assertEquals(1, mockModelAndView.getModel().get("max"));
        assertEquals(1, mockModelAndView.getModel().get("current"));

    }

    @Test
    void getNumber() {
        long result = accountTransactionService.getNumber();
        assertTrue(result >= 1_000_000_000L && result <= 9_999_999_999L);
    }

    @Test
    void clearAllScore() {
        Score mockScore = new Score();
        mockScore.setId(1L);
        List<AccountTransaction> mockTransactions = new ArrayList<>();
        AccountTransaction transactionWithScore = new AccountTransaction();
        transactionWithScore.setId(1L);
        transactionWithScore.setScore(mockScore);
        mockTransactions.add(transactionWithScore);

        when(accountTransactionRepository.findAll()).thenReturn(mockTransactions);
        when(accountTransactionRepository.save(any(AccountTransaction.class))).thenReturn(new AccountTransaction());

        accountTransactionService.clearAllScore(mockScore);

        assertEquals(null, transactionWithScore.getScore());
        verify(accountTransactionRepository, times(1)).findAll();
        verify(accountTransactionRepository, times(1)).save(any(AccountTransaction.class));
    }

    @Test
    void filter() {
        AccountTransactionForViewDTO filterDTO = new AccountTransactionForViewDTO();
        filterDTO.setId(1L);
        filterDTO.setDate(LocalDate.now());
        filterDTO.setUserName("");
        filterDTO.setScoreNumber("");
        filterDTO.setTransactionPurposeName("w");
        filterDTO.setAdminName("");
        filterDTO.setIsIncome(true);
        filterDTO.setAddToStats(true);
        filterDTO.setSum("");
        filterDTO.setComment("");
        filterDTO.setNumber("");

        List<AccountTransactionForViewDTO> mockDTOList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockDTOList.add(new AccountTransactionForViewDTO());
        }
        mockDTOList.get(1).setId(1L);
        mockDTOList.get(1).setDate(LocalDate.now());
        mockDTOList.get(1).setUserName("");
        mockDTOList.get(1).setScoreNumber("");
        mockDTOList.get(1).setTransactionPurposeName("www");
        mockDTOList.get(1).setAdminName("");
        mockDTOList.get(1).setIsIncome(true);
        mockDTOList.get(1).setAddToStats(true);
        mockDTOList.get(1).setSum("");
        mockDTOList.get(1).setComment("");
        mockDTOList.get(1).setNumber("");
        mockDTOList.get(2).setId(2L);
        mockDTOList.get(2).setScoreNumber("123456");
        mockDTOList.get(2).setDate(LocalDate.now());
        mockDTOList.get(3).setId(3L);
        mockDTOList.get(3).setTransactionPurposeName("Payment");
        mockDTOList.get(3).setDate(LocalDate.now());
        mockDTOList.get(4).setId(4L);
        mockDTOList.get(4).setAdminName("Admin");
        mockDTOList.get(4).setDate(LocalDate.now());

        List<AccountTransactionForViewDTO> result = accountTransactionService.filter(filterDTO, mockDTOList);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertTrue(result.get(0).getIsIncome());
    }

    @Test
    void excelWhereUserIsNull() throws IOException {
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setNumber("account transaction number");
        accountTransaction.setFromDate(new Date(1,1,1));
        accountTransaction.setAddToStats(true);
        TransactionPurpose transactionPurpose = new TransactionPurpose();
        transactionPurpose.setName("transaction purpose name");
        accountTransaction.setIncome(true);
        accountTransaction.setSum(12.0);
        accountTransaction.setTransactionPurpose(transactionPurpose);
        when(accountTransactionRepository.findAll()).thenReturn(Arrays.asList(accountTransaction));
        MockHttpServletResponse response = new MockHttpServletResponse();
        accountTransactionService.excel(response);
        byte[] content = response.getContentAsByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1);
        Iterator<Cell> cellIterator = row.cellIterator();
        String[] expectResult = new String[]{"account transaction number", "398.0", "Проведен", "transaction purpose name", "Не указан", "Не указан", "Приход", "12.0"};
        int index = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String cellValue = String.valueOf(cell);
            assertEquals(cellValue, expectResult[index++]);
        }
        assertEquals(8, index);
        assertEquals("application/vnd.ms-excel", response.getContentType());
        assertEquals("attachment; filename=example.xls", response.getHeader("Content-Disposition"));
    }
    @Test
    void excelWhereUserIsNotNull() throws IOException {
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setNumber("account transaction number");
        accountTransaction.setFromDate(new Date(1,1,1));
        accountTransaction.setAddToStats(true);
        TransactionPurpose transactionPurpose = new TransactionPurpose();
        transactionPurpose.setName("transaction purpose name");
        accountTransaction.setIncome(true);
        accountTransaction.setSum(12.0);
        accountTransaction.setTransactionPurpose(transactionPurpose);
        User user = new User();
        user.setFirstName("user name");
        Score score = new Score();
        score.setNumber("score number");
        accountTransaction.setUser(user);
        accountTransaction.setScore(score);
        when(accountTransactionRepository.findAll()).thenReturn(Arrays.asList(accountTransaction));
        MockHttpServletResponse response = new MockHttpServletResponse();
        accountTransactionService.excel(response);
        byte[] content = response.getContentAsByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1);
        Iterator<Cell> cellIterator = row.cellIterator();
        String[] expectResult = new String[]{"account transaction number", "398.0", "Проведен", "transaction purpose name", "user name", "score number", "Приход", "12.0"};
        int index = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String cellValue = String.valueOf(cell);
            assertEquals(cellValue, expectResult[index++]);
        }
        assertEquals(8, index);
        assertEquals("application/vnd.ms-excel", response.getContentType());
        assertEquals("attachment; filename=example.xls", response.getHeader("Content-Disposition"));
    }
}