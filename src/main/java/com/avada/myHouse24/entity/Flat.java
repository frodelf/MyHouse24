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
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne
    private Tariff tariff;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "score_id")
    @JsonIgnore
    private Score score;
    @OneToMany(mappedBy = "flat",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CounterData> counterData;
    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Invoice> invoices;
}
