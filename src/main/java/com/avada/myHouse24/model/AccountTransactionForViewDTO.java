package com.avada.myHouse24.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountTransactionForViewDTO {
    private Long id;
    private LocalDate date;
    private String userName;
    private String scoreNumber;
    private String transactionPurposeName;
    private String adminName;
    private Boolean isIncome;
    private Boolean addToStats;
    private String sum;
    private String comment;
    private String number;
}
