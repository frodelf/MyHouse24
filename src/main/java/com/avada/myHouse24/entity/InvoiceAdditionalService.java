package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice_additional_service")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceAdditionalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double price;
    private Double consumption;
    @ManyToOne
    private AdditionalService additionalService;
    @ManyToOne
    private Invoice invoice;
}
