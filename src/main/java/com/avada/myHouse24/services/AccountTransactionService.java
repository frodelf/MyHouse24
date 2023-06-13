package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.AccountTransaction;

import java.util.List;

public interface AccountTransactionService {
    List<AccountTransaction> getAll();
    long getMaxId();
    long getSumWhereIsIncomeIsTrue();
    long getSumWhereIsIncomeIsFalse();
    void save(AccountTransaction accountTransaction);
    AccountTransaction getById(long id);
    void deleteById(long id);
}
