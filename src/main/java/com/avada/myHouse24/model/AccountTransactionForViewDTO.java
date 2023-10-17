package com.avada.myHouse24.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class AccountTransactionForViewDTO {
    private Long id;
    private LocalDate date;
    @Length(max = 50)
    private String userName;
    @Length(max = 10)
    private String scoreNumber;
    @Length(max = 50)
    private String transactionPurposeName;
    @Length(max = 50)
    private String adminName;
    private Boolean isIncome;
    private Boolean addToStats;
    @Length(max =  10)
    private String sum;
    @Length(max = 50)
    private String comment;
    @Length(max = 10)
    private String number;
}
