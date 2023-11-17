package com.avada.myHouse24.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Admin sender;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "message_recipients",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id"))
    private List<User> recipients;
    private String recipientsName;
    private String date;
    @NotBlank(message = "Введите текст сообщения")
    @Size(max = 255, message = "Длина сообщения не должна превышать 255 символов")
    private String text;
    @NotBlank(message = "Введите тему сообщения")
    @Size(max = 30, message = "Длина темы не должна превышать 30 символов")
    private String topic;
    @PrePersist
    public void prePersist() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");
        this.date = LocalDateTime.now().format(formatter);
    }
}
