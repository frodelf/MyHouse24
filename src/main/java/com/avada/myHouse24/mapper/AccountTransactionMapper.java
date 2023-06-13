package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.AccountTransaction;
import com.avada.myHouse24.model.AccountTransactionDTO;
import com.avada.myHouse24.model.AccountTransactionInDTO;
import com.avada.myHouse24.model.AccountTransactionOutDTO;
import com.avada.myHouse24.repo.AccountTransactionRepository;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.ScoreServiceImpl;
import com.avada.myHouse24.services.impl.TransactionPurposeServiceImpl;
import com.avada.myHouse24.services.impl.UserServiceImpl;
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
    private final ScoreServiceImpl scoreService;
    private final AdminServiceImpl adminService;
    private final TransactionPurposeServiceImpl transactionPurposeService;

    public AccountTransactionInDTO toDtoForIn(AccountTransaction accountTransaction){
        return new AccountTransactionInDTO(accountTransaction.getId().toString(), accountTransaction.getFromDate(),
                accountTransaction.getTransactionPurpose().getName(),
                accountTransaction.getUser().getFirstName(), accountTransaction.getScore().getId().toString(),
                accountTransaction.isIncome(), String.valueOf(accountTransaction.getSum()), accountTransaction.getAdmin().getName(), accountTransaction.getComment(), accountTransaction.isAddToStats());
    }

    public AccountTransaction toEntityForIn(AccountTransactionInDTO accountTransactionDTOIn){
        return new AccountTransaction(Long.parseLong(accountTransactionDTOIn.getId()), accountTransactionDTOIn.getDate(), Double.valueOf(accountTransactionDTOIn.getSum()), accountTransactionDTOIn.isIncome(), accountTransactionDTOIn.isAddToStats(), accountTransactionDTOIn.getComment(),
                userService.getByFirstName(accountTransactionDTOIn.getUserName()), scoreService.getById(Long.parseLong(accountTransactionDTOIn.getScoreId())), adminService.getByName(accountTransactionDTOIn.getAdminName()), transactionPurposeService.getByName(accountTransactionDTOIn.getTransactionPurposeName()));
    }
    public AccountTransactionOutDTO toDtoForOut(AccountTransaction accountTransaction){
        return new AccountTransactionOutDTO(accountTransaction.getId().toString(), accountTransaction.getFromDate(), accountTransaction.getTransactionPurpose().getName(), accountTransaction.isIncome(), String.valueOf(accountTransaction.getSum()), accountTransaction.getAdmin().getName(), accountTransaction.getComment(), accountTransaction.isAddToStats());
    }
    public AccountTransaction toEntityForOut(AccountTransactionOutDTO accountTransactionOutDTO){
        return new AccountTransaction(Long.parseLong(accountTransactionOutDTO.getId()), accountTransactionOutDTO.getDate(), Double.valueOf(accountTransactionOutDTO.getSum()), accountTransactionOutDTO.isIncome(), accountTransactionOutDTO.isAddToStats(), accountTransactionOutDTO.getComment(), adminService.getByName(accountTransactionOutDTO.getAdminName()), transactionPurposeService.getByName(accountTransactionOutDTO.getTransactionPurposeName()));
    }
    public AccountTransaction toEntity(AccountTransactionDTO accountTransactionDTO){
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setId(Long.valueOf(accountTransactionDTO.getId()));
        accountTransaction.setFromDate(Date.valueOf(accountTransactionDTO.getDate()));
        if(!accountTransactionDTO.getUserName().isBlank())accountTransaction.setUser(userService.getByFirstName(accountTransactionDTO.getUserName()));
        if(!accountTransactionDTO.getScoreId().isBlank())accountTransaction.setScore(scoreService.getById(Long.parseLong(accountTransactionDTO.getScoreId())));
        accountTransaction.setTransactionPurpose(transactionPurposeService.getByName(accountTransactionDTO.getTransactionPurposeName()));
        accountTransaction.setAdmin(adminService.getByName(accountTransactionDTO.getAdminName()));
        accountTransaction.setIncome(accountTransactionDTO.getIsIncome());
        accountTransaction.setSum(Double.parseDouble(accountTransactionDTO.getSum()));
        accountTransaction.setComment(accountTransactionDTO.getComment());
        accountTransaction.setAddToStats(accountTransactionDTO.getIsIncome());
        return accountTransaction;
    }
    public AccountTransactionDTO toDto(AccountTransaction accountTransaction){
        AccountTransactionDTO accountTransactionDTO = new AccountTransactionDTO();
        accountTransactionDTO.setId(String.valueOf(accountTransaction.getId()));
        accountTransactionDTO.setDate(String.valueOf(accountTransaction.getFromDate()));
        try {
            accountTransactionDTO.setUserName(accountTransaction.getUser().getFirstName());
        }catch (Exception e){
            log.warn(e);
            log.warn("AccountTransaction hasn't user.");
            accountTransactionDTO.setUserName("Не указан");
        }
        try {
            accountTransactionDTO.setScoreId(String.valueOf(accountTransaction.getScore().getId()));
        }catch (Exception e){
            log.warn(e);
            log.warn("AccountTransaction hasn't  score.");
            accountTransactionDTO.setScoreId("Не указан");
        }
        accountTransactionDTO.setTransactionPurposeName(accountTransaction.getTransactionPurpose().getName());
        accountTransactionDTO.setAdminName(accountTransaction.getAdmin().getName());
        accountTransactionDTO.setIsIncome(accountTransaction.isIncome());
        accountTransactionDTO.setSum(String.valueOf(accountTransaction.getSum()));
        accountTransactionDTO.setComment(accountTransaction.getComment());
        accountTransactionDTO.setAddToStats(accountTransaction.isAddToStats());
        return accountTransactionDTO;
    }
    public List<AccountTransactionDTO> toDtoList(List<AccountTransaction> accountTransactions){
        ArrayList<AccountTransactionDTO> accountTransactionDTOS = new ArrayList<>();
        for (AccountTransaction accountTransaction : accountTransactions) {
            accountTransactionDTOS.add(toDto(accountTransaction));
        }
        return accountTransactionDTOS;
    }
}
