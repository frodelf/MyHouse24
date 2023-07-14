package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class FlatDTO {
    private Long id;
    @NotNull(message = "Поле не може бути пустим")
    private Long number;
    @NotNull(message = "Поле не може бути пустим")
    private Double area;
    @NotNull(message = "Поле не може бути пустим")
    private House house;
    @NotNull(message = "Поле не може бути пустим")
    private Section section;
    @NotNull(message = "Поле не може бути пустим")
    private Floor floor;
    @NotNull(message = "Поле не може бути пустим")
    private User user;
    @NotNull(message = "Поле не може бути пустим")
    private Tariff tariff;
    @NotNull(message = "Поле не може бути пустим")
    private String scoreNumber;
}
