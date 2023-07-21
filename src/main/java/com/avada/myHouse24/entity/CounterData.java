package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "counter_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "flat")
public class CounterData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String number;
    private String status;
    private double data;
    @Column(name = "from_date")
    private Date fromDate;
    @ManyToOne
    @JoinColumn(name = "flat_id")
    private Flat flat;
    @ManyToOne
    private AdditionalService additionalService;
}
