package com.avada.myHouse24.model;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class UserForViewDTO {
    private String id;
    private String fullName;
    private String phone;
    private String email;
    private List<String> houses;
    private List<String> flats;
    private Date date;
    private String status;
    private Boolean isDebt;
}
