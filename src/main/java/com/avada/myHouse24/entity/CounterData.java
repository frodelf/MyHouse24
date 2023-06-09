package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "counter_data")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CounterData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String status;
    private double data;
    @Column(name = "from_date")
    private Date fromDate;
    @ManyToOne
    private Flat flat;
    @ManyToOne
    private AdditionalService additionalService;
}
