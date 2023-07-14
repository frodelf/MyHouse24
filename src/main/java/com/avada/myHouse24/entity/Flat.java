package com.avada.myHouse24.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "flat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "house")

public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private int number;
    private double area;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    @JsonIgnore
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
    @JoinColumn(name = "score_id")
    private Score score;
    @OneToMany
    private List<CounterData> counterData;
    @OneToMany
    private List<Invoice> invoices;
}
