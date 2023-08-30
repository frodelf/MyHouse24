package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.TransactionPurpose;
import com.avada.myHouse24.repo.TransactionPurposeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionPurposeServiceImplTest {
    @InjectMocks
    private TransactionPurposeServiceImpl transactionPurposeService;
    @Mock
    private TransactionPurposeRepository transactionPurposeRepository;
    @Test
    void getByName() {
        String mockName = "Income";
        TransactionPurpose mockTransactionPurpose = new TransactionPurpose();
        when(transactionPurposeRepository.findByName(mockName)).thenReturn(Optional.of(mockTransactionPurpose));
        TransactionPurpose result = transactionPurposeService.getByName(mockName);
        assertNotNull(result);
        verify(transactionPurposeRepository, times(1)).findByName(mockName);
    }

    @Test
    void getById() {
        long mockId = 1L;
        TransactionPurpose mockTransactionPurpose = new TransactionPurpose();
        when(transactionPurposeRepository.findById(mockId)).thenReturn(Optional.of(mockTransactionPurpose));
        TransactionPurpose result = transactionPurposeService.getById(mockId);
        assertNotNull(result);
        verify(transactionPurposeRepository, times(1)).findById(mockId);
    }

    @Test
    void getAll() {
        List<TransactionPurpose> mockPurposes = Arrays.asList(new TransactionPurpose(), new TransactionPurpose());
        when(transactionPurposeRepository.findAll()).thenReturn(mockPurposes);
        List<TransactionPurpose> result = transactionPurposeService.getAll();
        assertEquals(mockPurposes.size(), result.size());
        verify(transactionPurposeRepository, times(1)).findAll();
    }

    @Test
    void getAllIncomeTrue() {
        TransactionPurpose transactionPurpose1 = new TransactionPurpose();
        transactionPurpose1.setIncome(true);
        TransactionPurpose transactionPurpose2 = new TransactionPurpose();
        transactionPurpose2.setIncome(false);
        List<TransactionPurpose> mockPurposes = Arrays.asList(transactionPurpose1, transactionPurpose2);
        when(transactionPurposeRepository.findAll()).thenReturn(mockPurposes);
        List<TransactionPurpose> result = transactionPurposeService.getAllIncomeTrue();
        assertEquals(1, result.size());
        assertTrue(result.get(0).isIncome());
        verify(transactionPurposeRepository, times(1)).findAll();
    }

    @Test
    void getAllIncomeFalse() {
        TransactionPurpose transactionPurpose1 = new TransactionPurpose();
        transactionPurpose1.setIncome(true);
        TransactionPurpose transactionPurpose2 = new TransactionPurpose();
        transactionPurpose2.setIncome(false);
        List<TransactionPurpose> mockPurposes = Arrays.asList(transactionPurpose1, transactionPurpose2);
        when(transactionPurposeRepository.findAll()).thenReturn(mockPurposes);
        List<TransactionPurpose> result = transactionPurposeService.getAllIncomeFalse();
        assertEquals(1, result.size());
        assertTrue(!result.get(0).isIncome());
        verify(transactionPurposeRepository, times(1)).findAll();
    }

    @Test
    void save() {
        TransactionPurpose mockTransactionPurpose = new TransactionPurpose();
        transactionPurposeService.save(mockTransactionPurpose);
        verify(transactionPurposeRepository, times(1)).save(mockTransactionPurpose);
    }

    @Test
    void deleteById() {
        transactionPurposeService.deleteById(1);
        verify(transactionPurposeRepository, times(1)).deleteById(anyLong());
    }
}