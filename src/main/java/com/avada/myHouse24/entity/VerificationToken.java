package com.avada.myHouse24.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDate createdAt;
    private LocalDate expiresAt;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.createdAt = LocalDate.now();
        this.expiresAt = LocalDate.now().plusDays(1);
    }

    public boolean isValid() {
        return (LocalDate.now().isBefore(this.expiresAt));
    }
}