package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.model.AccountTransactionForViewDTO;
import com.avada.myHouse24.model.AccountTransactionInDTO;
import com.avada.myHouse24.model.AccountTransactionOutDTO;
import com.avada.myHouse24.repo.AccountTransactionRepository;
import com.avada.myHouse24.services.impl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class AccountTransactionMapper {
    private final AccountTransactionRepository accountTransactionRepository;
    private final UserServiceImpl userService;
    private final AccountTransactionServiceImpl accountTransactionService;
    private final ScoreServiceImpl scoreService;
    private final AdminServiceImpl adminService;
    private final TransactionPurposeServiceImpl transactionPurposeService;

    public AccountTransactionInDTO toDtoForIn(AccountTransaction accountTransaction){
        return new AccountTransactionInDTO(accountTransaction.getId(), accountTransaction.getFromDate(),
                accountTransaction.getTransactionPurpose().getName(),
                accountTransaction.getUser().getFirstName(), accountTransaction.getScore().getId().toString(),
                accountTransaction.isIncome(), String.valueOf(accountTransaction.getSum()), accountTransaction.getAdmin().getFirstName(), accountTransaction.getComment(), accountTransaction.isAddToStats(), accountTransaction.getNumber());
    }

    public AccountTransaction toEntityForIn(AccountTransactionInDTO accountTransactionDTOIn){
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setNumber(accountTransactionDTOIn.getNumber());
        accountTransaction.setId(accountTransactionDTOIn.getId());
        accountTransaction.setFromDate(accountTransactionDTOIn.getDate());
        accountTransaction.setSum(Double.valueOf(accountTransactionDTOIn.getSum()));
        accountTransaction.setIncome(accountTransactionDTOIn.isIncome());
        accountTransaction.setAddToStats(accountTransactionDTOIn.isAddToStats());
        accountTransaction.setComment(accountTransactionDTOIn.getComment());
        accountTransaction.setUser(userService.getByFirstName(accountTransactionDTOIn.getUserName()));
        accountTransaction.setScore(scoreService.getById(Long.parseLong(accountTransactionDTOIn.getScoreId())));
        accountTransaction.setAdmin(adminService.getByName(accountTransactionDTOIn.getAdminName()));
        accountTransaction.setTransactionPurpose(transactionPurposeService.getByName(accountTransactionDTOIn.getTransactionPurposeName()));

        return accountTransaction;

    }
    public AccountTransactionOutDTO toDtoForOut(AccountTransaction accountTransaction){
        return new AccountTransactionOutDTO(accountTransaction.getId(), accountTransaction.getFromDate(), accountTransaction.getTransactionPurpose().getName(), accountTransaction.isIncome(), String.valueOf(accountTransaction.getSum()), accountTransaction.getAdmin().getFirstName(), accountTransaction.getComment(), accountTransaction.getNumber(), accountTransaction.isAddToStats());
    }
    public AccountTransaction toEntityForOut(AccountTransactionOutDTO accountTransactionOutDTO) {
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setNumber(accountTransactionOutDTO.getNumber());
        accountTransaction.setId(accountTransactionOutDTO.getId());
        accountTransaction.setFromDate(accountTransactionOutDTO.getDate());
        accountTransaction.setSum(Double.valueOf(accountTransactionOutDTO.getSum()));
        accountTransaction.setIncome(accountTransactionOutDTO.isIncome());
        accountTransaction.setAddToStats(accountTransactionOutDTO.isAddToStats());
        accountTransaction.setComment(accountTransactionOutDTO.getComment());
        accountTransaction.setAdmin(adminService.getByName(accountTransactionOutDTO.getAdminName()));
        accountTransaction.setTransactionPurpose(transactionPurposeService.getByName(accountTransactionOutDTO.getTransactionPurposeName()));
        return accountTransaction;
    }

    public AccountTransaction toEntityForView(AccountTransactionForViewDTO accountTransactionForViewDTO){
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setId(accountTransactionForViewDTO.getId());
        accountTransaction.setNumber(accountTransactionForViewDTO.getNumber());
        accountTransaction.setFromDate(Date.valueOf(accountTransactionForViewDTO.getDate()));
        if(!accountTransactionForViewDTO.getUserName().isBlank())accountTransaction.setUser(userService.getByFirstName(accountTransactionForViewDTO.getUserName()));
        if(!accountTransactionForViewDTO.getScoreNumber().isBlank())accountTransaction.setScore(scoreService.getById(Long.parseLong(accountTransactionForViewDTO.getScoreNumber())));
        accountTransaction.setTransactionPurpose(transactionPurposeService.getByName(accountTransactionForViewDTO.getTransactionPurposeName()));
        accountTransaction.setAdmin(adminService.getByName(accountTransactionForViewDTO.getAdminName()));
        accountTransaction.setIncome(accountTransactionForViewDTO.getIsIncome());
        accountTransaction.setSum(Double.parseDouble(accountTransactionForViewDTO.getSum()));
        accountTransaction.setComment(accountTransactionForViewDTO.getComment());
        accountTransaction.setAddToStats(accountTransactionForViewDTO.getIsIncome());
        return accountTransaction;
    }
    public AccountTransactionForViewDTO toDtoForView(AccountTransaction accountTransaction){
        AccountTransactionForViewDTO accountTransactionForViewDTO = new AccountTransactionForViewDTO();
        accountTransactionForViewDTO.setId(accountTransaction.getId());
        accountTransactionForViewDTO.setNumber(accountTransaction.getNumber());
        accountTransactionForViewDTO.setDate(String.valueOf(accountTransaction.getFromDate()));
        try {
            accountTransactionForViewDTO.setUserName(accountTransaction.getUser().getFirstName());
        }catch (Exception e){
            log.warn(e);
            log.warn("AccountTransaction hasn't user.");
            accountTransactionForViewDTO.setUserName("Не указан");
        }
        try {
            accountTransactionForViewDTO.setScoreNumber(String.valueOf(accountTransaction.getScore().getId()));
        }catch (Exception e){
            log.warn(e);
            log.warn("AccountTransaction hasn't  score.");
            accountTransactionForViewDTO.setScoreNumber("Не указан");
        }
        accountTransactionForViewDTO.setTransactionPurposeName(accountTransaction.getTransactionPurpose().getName());
        accountTransactionForViewDTO.setAdminName(accountTransaction.getAdmin().getFirstName());
        accountTransactionForViewDTO.setIsIncome(accountTransaction.isIncome());
        accountTransactionForViewDTO.setSum(String.valueOf(accountTransaction.getSum()));
        accountTransactionForViewDTO.setComment(accountTransaction.getComment());
        accountTransactionForViewDTO.setAddToStats(accountTransaction.isAddToStats());
        return accountTransactionForViewDTO;
    }
    public List<AccountTransactionForViewDTO> toDtoForViewList(List<AccountTransaction> accountTransactions){
        ArrayList<AccountTransactionForViewDTO> accountTransactionForViewDTOS = new ArrayList<>();
        for (AccountTransaction accountTransaction : accountTransactions) {
            accountTransactionForViewDTOS.add(toDtoForView(accountTransaction));
        }
        return accountTransactionForViewDTOS;
    }
}
