package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.model.AccountTransactionForViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

public interface AccountTransactionService {
    List<AccountTransaction> getAll();
    long getMaxId();
    long getSumWhereIsIncomeIsTrue();
    long getSumWhereIsIncomeIsFalse();
    void save(AccountTransaction accountTransaction);
    AccountTransaction getById(long id);
    void deleteById(long id);
    AccountTransaction getByNumber(String number);
    Page<AccountTransactionForViewDTO> getPage(int pageNumber, Model model, List<AccountTransactionForViewDTO> userList);
    Page<AccountTransaction> getPage(int pageNumber, Model model);
    List<AccountTransactionForViewDTO> filterByDateRange(List<AccountTransactionForViewDTO> transactions, String dateRange);
    long getNumber();
}
