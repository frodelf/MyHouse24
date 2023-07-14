package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.AdditionalService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffDTO {
    private Long id;
    @NotBlank(message = "Поле не може бути пустим")
    @Size(max = 50, message = "Поле не може містити більше ніж 50 символів")
    private String name;
    @NotBlank(message = "Поле не може бути пустим")
    @Size(max = 500, message = "Поле не може містити більше ніж 500 символів")
    private String description;
    private List<AdditionalServiceForTariffDTO> additionalServiceForTariffDTOS;
    private List<String> names;
}
