package com.avada.myHouse24.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;

@Entity
public class PersistentLogins {
    @Id
    private String series;
    private String username;
    private String token;
    private Date lastUsed;
}