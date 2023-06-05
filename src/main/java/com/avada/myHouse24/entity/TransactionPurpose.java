package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_purpose")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionPurpose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "is_income")
    private boolean isIncome;
    private String name;

}
