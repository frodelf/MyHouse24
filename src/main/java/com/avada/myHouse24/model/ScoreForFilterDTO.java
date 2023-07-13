package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Section;
import com.avada.myHouse24.entity.User;
import lombok.Data;

@Data
public class ScoreForFilterDTO {
    private String number;
    private String status;
    private Long flatNumber;
    private House house;
    private Section section;
    private User user;
    private Boolean isDebt;
}
