package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.Flat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MasterRequestDTO {
    private Long id;
    @NotBlank(message = "Поле повинно бути заповнене")
    private String role;
    @NotNull(message = "Поле повинно бути заповнене")
    private Flat flat;
    @NotNull(message = "Поле повинно бути заповнене")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "Поле повинно бути заповнене")
    private Date time;
    @NotBlank(message = "Поле повинно бути заповнене")
    private String description;
    private String status;
}

