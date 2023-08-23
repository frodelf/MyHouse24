package com.avada.myHouse24.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "tariff_additional_service")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "tariff")
public class TariffAdditionalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long price;
    @ManyToOne
    @JsonIgnore
    private Tariff tariff;
    @ManyToOne
    private AdditionalService additionalService;
}

