package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "additional_service")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdditionalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    @Column(name = "show_in_counter")
    private boolean showInCounter;
    @ManyToOne
    private UnitOfMeasurement unitOfMeasurement;
}
