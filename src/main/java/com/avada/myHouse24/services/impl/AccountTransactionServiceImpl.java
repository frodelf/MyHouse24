package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.model.AccountTransactionForViewDTO;
import com.avada.myHouse24.repo.AccountTransactionRepository;
import com.avada.myHouse24.services.AccountTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    @Override
    public AccountTransaction getByNumber(String number) {
        List<AccountTransaction> accountTransactions = accountTransactionRepository.findAll();
        for (AccountTransaction accountTransaction : accountTransactions) {
            if (accountTransaction.getNumber().equals(number)){
                return accountTransaction;
            }
        }
        throw new RuntimeException("По такому номеру касси не знайдено");
    }
    public List<AccountTransactionForViewDTO> filterByDateRange(List<AccountTransactionForViewDTO> transactions, String dateRange) {
        String[] range = dateRange.split(" to ");
        LocalDate startDate = LocalDate.parse(range[0]);
        LocalDate endDate = LocalDate.parse(range[1]);
        ArrayList<AccountTransactionForViewDTO> filteredTransactions = new ArrayList<>();
        for (AccountTransactionForViewDTO transaction : transactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ISO_DATE);
            if (transactionDate.isEqual(startDate) || transactionDate.isEqual(endDate) || (transactionDate.isAfter(startDate) && transactionDate.isBefore(endDate))) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
    public Page<AccountTransaction> getPage(int pageNumber, Model model) {
        double size = 10.0;
        int max = (int)Math.ceil(accountTransactionRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(accountTransactionRepository.findAll().size()/size-1) : 0;
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return accountTransactionRepository.findAll(pageRequest);
    }
    public Page<AccountTransactionForViewDTO> getPage(int pageNumber, Model model, List<AccountTransactionForViewDTO> userList) {
        double size = 10.0;
        int max = (int)Math.ceil(accountTransactionRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(accountTransactionRepository.findAll().size()/size-1) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, userList.size());
        List<AccountTransactionForViewDTO> pageList = userList.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<AccountTransactionForViewDTO> page = new PageImpl<>(pageList, pageable, userList.size());
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return page;
    }
    public long getNumber() {
        long min = 1_000_000_000L;
        long max = 9_999_999_999L;
        long randomNumber = min + (long) (Math.random() * (max - min));
        return randomNumber;
    }

    public void clearAllScore(Score score){
        List<AccountTransaction> accountTransactions = accountTransactionRepository.findAll();
        for (AccountTransaction accountTransaction : accountTransactions) {
            if(accountTransaction.getScore() != null && accountTransaction.getScore().getId() == score.getId()){
                accountTransaction.setScore(null);
                accountTransactionRepository.save(accountTransaction);
            }
        }
    }
}
