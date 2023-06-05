package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first-name")
    private String firstName;
    private String name;
    @Column(name = "second-name")
    private String secondName;
    private Date birthday;
    private String phone;
    private String viber;
    private String telegram;
    private String email;
    private String password;
    private String status;
    private String description;
    private String image;
    @OneToMany
    List<Flat> flats;
}
