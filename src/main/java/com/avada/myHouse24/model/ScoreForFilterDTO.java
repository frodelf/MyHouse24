package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Section;
import com.avada.myHouse24.entity.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ScoreForFilterDTO {
    @Length(max = 10)
    private String number;
    @Length(max = 50)
    private String status;
    private Long flatNumber;
    private House house;
    private Section section;
    private User user;
    private Boolean isDebt;
}
