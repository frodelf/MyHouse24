package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.mapper.AccountTransactionMapper;
import com.avada.myHouse24.model.AccountTransactionForViewDTO;
import com.avada.myHouse24.repo.AccountTransactionRepository;
import com.avada.myHouse24.services.AccountTransactionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Page<AccountTransaction> getPage(int pageNumber, ModelAndView model) {
        double size = 10.0;
        int max = (int)Math.ceil(accountTransactionRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(accountTransactionRepository.findAll().size()/size-1) : 0;
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addObject("max", max);
        model.addObject("current", pageNumber+1);
        return accountTransactionRepository.findAll(pageRequest);
    }
    public Page<AccountTransactionForViewDTO> getPage(int pageNumber, ModelAndView model, List<AccountTransactionForViewDTO> userList) {
        double size = 10.0;
        int max = (int)Math.ceil(accountTransactionRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(accountTransactionRepository.findAll().size()/size-1) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, userList.size());
        List<AccountTransactionForViewDTO> pageList = userList.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<AccountTransactionForViewDTO> page = new PageImpl<>(pageList, pageable, userList.size());
        model.addObject("max", max);
        model.addObject("current", pageNumber+1);
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
    public List<AccountTransactionForViewDTO> filter(AccountTransactionForViewDTO accountTransactionForViewDTO, List<AccountTransactionForViewDTO> accountTransactions){


        if (accountTransactionForViewDTO.getId() != null) {
            accountTransactions = accountTransactions.stream()
                    .filter(dto -> dto.getId() != null && dto.getId().toString().contains(accountTransactionForViewDTO.getId().toString()))
                    .collect(Collectors.toList());
        }
        if (!accountTransactionForViewDTO.getDate().isBlank()) {
            accountTransactions = filterByDateRange(accountTransactions, accountTransactionForViewDTO.getDate());
        }
        if (accountTransactionForViewDTO.getScoreNumber() != null) {
            accountTransactions = accountTransactions.stream()
                    .filter(dto -> dto.getScoreNumber() != null && dto.getScoreNumber().contains(accountTransactionForViewDTO.getScoreNumber()))
                    .collect(Collectors.toList());
        }
        if (!accountTransactionForViewDTO.getTransactionPurposeName().isBlank()) {
            accountTransactions = accountTransactions.stream()
                    .filter(dto -> dto.getTransactionPurposeName() != null && dto.getTransactionPurposeName().contains(accountTransactionForViewDTO.getTransactionPurposeName()))
                    .collect(Collectors.toList());
        }
        if (accountTransactionForViewDTO.getAdminName() != null) {
            accountTransactions = accountTransactions.stream()
                    .filter(dto -> dto.getAdminName() != null && dto.getAdminName().contains(accountTransactionForViewDTO.getAdminName()))
                    .collect(Collectors.toList());
        }
        if (accountTransactionForViewDTO.getIsIncome() != null) {
            accountTransactions = accountTransactions.stream()
                    .filter(dto -> dto.getIsIncome() != null && dto.getIsIncome() == accountTransactionForViewDTO.getIsIncome())
                    .collect(Collectors.toList());
        }
        if (accountTransactionForViewDTO.getAddToStats() != null) {
            accountTransactions = accountTransactions.stream()
                    .filter(dto -> dto.getAddToStats() != null && dto.getAddToStats() == accountTransactionForViewDTO.getAddToStats())
                    .collect(Collectors.toList());
        }
        return accountTransactions;
    }
    public void excel(HttpServletResponse response) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("page1");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"№", "Дата", "Статус", "Тип платежа", "Владелец", "Лицевой счет", "Приход/расход", "Сумма (грн)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        List<AccountTransaction> accountTransactions = getAll();
        for (int rowIndex = 0; rowIndex < accountTransactions.size(); rowIndex++) {
            AccountTransaction transaction = accountTransactions.get(rowIndex);
            Row dataRow = sheet.createRow(rowIndex + 1);

            dataRow.createCell(0).setCellValue(transaction.getNumber());
            dataRow.createCell(1).setCellValue(transaction.getFromDate());
            dataRow.createCell(2).setCellValue(transaction.isAddToStats() ? "Проведен" : "Не проведен");
            dataRow.createCell(3).setCellValue(transaction.getTransactionPurpose().getName());
            if(transaction.getUser() != null) {
                dataRow.createCell(4).setCellValue(transaction.getUser().getFirstName());
                dataRow.createCell(5).setCellValue(transaction.getScore().getNumber());
            }
            else{
                dataRow.createCell(4).setCellValue("Не указан");
                dataRow.createCell(5).setCellValue("Не указан");
            }
            dataRow.createCell(6).setCellValue(transaction.isIncome() ? "Приход" : "Расход");
            dataRow.createCell(7).setCellValue(transaction.getSum());
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=example.xls");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}