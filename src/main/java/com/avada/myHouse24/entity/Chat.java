package com.avada.myHouse24.entity;

import com.avada.myHouse24.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "chat")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(length = 1000)
    private String text;
    private String fromName;
    private Long fromId;
    private Boolean isUser;
    private LocalDate date;
    private MessageType messageType;
    @ManyToOne
    private House house;
}
