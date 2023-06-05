package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "invoice")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String status;
    @Column(name = "from_date")
    private Date fromDate;
    @Column(name = "to_date")
    private Date toDate;
    private double sum;
    @Column(name = "add_to_stats")
    private boolean addToStats;
    @ManyToOne
    private Flat flat;
    @ManyToOne
    private Tariff tariff;
    @ManyToOne
    private Score score;
    @ManyToMany
    private List<AdditionalService> additionalService;
}
