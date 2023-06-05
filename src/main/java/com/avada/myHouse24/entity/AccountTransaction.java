package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "account_transaction")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "from-date")
    private Date fromDate;
    private double sum;
    @Column(name = "is_income")
    private boolean isIncome;
    @Column(name = "add_to_stats")
    private boolean addToStats;
    private String status;
    @Column(length = 2000)
    private String comment;
    @ManyToOne
    private User user;
    @ManyToOne
    private Score score;
    @ManyToOne
    private Admin admin;
    @ManyToOne
    private TransactionPurpose transactionPurpose;
}
