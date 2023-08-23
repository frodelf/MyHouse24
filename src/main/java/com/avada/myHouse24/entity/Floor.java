package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "floor")
@NoArgsConstructor
@Data
public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;

    public Floor(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Floor(String name) {
        this.name = name;
    }
}
