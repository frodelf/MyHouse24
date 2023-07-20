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
    @Column(name = "from_date")
    private Date fromDate;
    @Column(unique = true)
    private String number;
    private double sum;
    @Column(name = "is_income")
    private boolean isIncome;
    @Column(name = "add_to_stats")
    private boolean addToStats;
    @Column(length = 2000)
    private String comment;
    @ManyToOne
    private User user;
    @ManyToOne(cascade = CascadeType.DETACH)
    private Score score;
    @ManyToOne
    private Admin admin;
    @ManyToOne
    private TransactionPurpose transactionPurpose;

    public AccountTransaction(Long id, Date fromDate, double sum, boolean isIncome, boolean addToStats, String comment, Admin admin, TransactionPurpose transactionPurpose) {
        this.id = id;
        this.fromDate = fromDate;
        this.sum = sum;
        this.isIncome = isIncome;
        this.addToStats = addToStats;
        this.comment = comment;
        this.admin = admin;
        this.transactionPurpose = transactionPurpose;
    }
}
