package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.AdditionalService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalServiceForTariffDTO {
    private AdditionalService additionalService;
    private Long price;
}
