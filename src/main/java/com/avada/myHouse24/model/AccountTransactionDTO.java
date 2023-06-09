package com.avada.myHouse24.model;

import lombok.Data;

import java.sql.Date;

@Data
public class AccountTransactionDTO {
    private String id;
    private Date date;
    private String userName;//
    private String scoreId;//
    private String transactionPurposeName;
    private String adminName;
    private boolean isIncome;
    private boolean addToStats;
    private String sum;
    private String comment;

}
