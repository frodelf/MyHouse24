package com.avada.myHouse24.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UnitOfMeasurementDTO {
    private String id;
    @NotBlank(message = "Поле не може бути порожнім")
    @Size(max = 50, message = "Поле не може бути більше 50 символів")
    private String name;
    private String error;
}
