package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "tariff")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String description;
    @Column(name = "date_edit")
    private Date dateEdit;
    @ManyToMany
    private List<AdditionalService> additionalServices;
}
