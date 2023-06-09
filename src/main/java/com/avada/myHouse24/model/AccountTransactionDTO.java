package com.avada.myHouse24.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountTransactionDTO {
    private long id;
    private Date date;
    private String status;
    private String transactionPurposeName;
    private String userName;
    private long scoreId;
    private boolean isIncome;
    private double sum;
}
