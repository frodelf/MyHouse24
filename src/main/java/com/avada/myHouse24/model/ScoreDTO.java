package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Section;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScoreDTO {
    private Long id;
    @NotBlank(message = "Поле не може бути пустим")
    private String number;
    @NotNull(message = "Поле не може бути пустим")
    private String status;
    @NotNull(message = "Поле не може бути пустим")
    private House house;
    @NotNull(message = "Поле не може бути пустим")
    private Section section;
    private Flat flat;
    private Double balance;
}
