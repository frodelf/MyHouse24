package com.avada.myHouse24.model;

import lombok.Data;

import java.sql.Date;

@Data
public class AccountTransactionDTO {
    private String id;
    private String date;
    private String userName;//
    private String scoreId;//
    private String transactionPurposeName;
    private String adminName;
    private Boolean isIncome;
    private Boolean addToStats;
    private String sum;
    private String comment;

}
