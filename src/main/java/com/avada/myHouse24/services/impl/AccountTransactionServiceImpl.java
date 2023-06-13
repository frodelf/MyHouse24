package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.repository.AccountTransactionRepository;
import com.avada.myHouse24.services.AccountTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AccountTransactionServiceImpl implements AccountTransactionService {
    private final AccountTransactionRepository accountTransactionRepository;
    @Override
    public List<AccountTransaction> getAll() {
        return accountTransactionRepository.findAll();
    }
    @Override
    public long getMaxId() {
        long id;
        try {
            id = accountTransactionRepository.findMaxId();
        } catch (Exception e) {
            id = 0;
        }
        return ++id;
    }

    @Override
    public long getSumWhereIsIncomeIsTrue() {
        Long sum = accountTransactionRepository.sumWhereIsIncomeIsTrue();
        if(sum == null || sum <= 0) return 0;
        return sum;
    }

    @Override
    public long getSumWhereIsIncomeIsFalse() {
        Long sum = accountTransactionRepository.sumWhereIsIncomeIsFalse();
        if(sum == null || sum <= 0) return 0;
        return sum;
    }


    @Override
    public void save(AccountTransaction accountTransaction) {
        accountTransactionRepository.save(accountTransaction);
    }

    @Override
    public AccountTransaction getById(long id) {
        return accountTransactionRepository.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        accountTransactionRepository.deleteById(id);
    }
}
