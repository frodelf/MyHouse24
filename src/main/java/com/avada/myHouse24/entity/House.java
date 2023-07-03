package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table(name = "house")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String address;
    private String image;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Flat> flats;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Section> sections;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Floor> floors;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Admin> admins;
}
