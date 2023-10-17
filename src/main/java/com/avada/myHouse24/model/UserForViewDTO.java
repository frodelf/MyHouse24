package com.avada.myHouse24.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;
import java.util.List;

@Data
public class UserForViewDTO {
    @Length(max = 10)
    private String id;
    @Length(max = 50)
    private String fullName;
    @Length(max = 10)
    private String phone;
    @Length(max = 50)
    private String email;
    private List<String> houses;
    private List<String> flats;
    private Date date;
    private String status;
    private Boolean isDebt;
}
