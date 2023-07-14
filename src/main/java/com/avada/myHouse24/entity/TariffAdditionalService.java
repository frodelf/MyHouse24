package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "tariff_additional_service")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TariffAdditionalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long price;
    @ManyToOne
    private Tariff tariff;
    @ManyToOne
    private AdditionalService additionalService;
}

