package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "flat")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private int number;
    private double area;
    @ManyToOne
    private House house;
    @ManyToOne
    private Floor floor;
    @ManyToOne
    private Section section;
    @ManyToOne
    private User user;
    @ManyToOne
    private Tariff tariff;
    @OneToOne(cascade = CascadeType.ALL)
    private Score score;
    @OneToMany
    private List<CounterData> counterData;
    @OneToMany
    private List<Invoice> invoices;
}
