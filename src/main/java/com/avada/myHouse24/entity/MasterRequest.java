package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "master_request")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MasterRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String description;
    private String status;
    @Column(length = 1000)
    private String comment;
    @ManyToOne
    private Admin admin;
    @ManyToOne
    private User user;
    @ManyToOne
    private Flat flat;
}
