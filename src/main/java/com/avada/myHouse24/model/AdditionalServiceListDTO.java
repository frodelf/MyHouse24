package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.AccountTransaction;
import lombok.Data;

import java.util.List;

@Data
public class AdditionalServiceListDTO {
    private List<AdditionalServiceDTO> services;
    private List<UnitOfMeasurementDTO> unitOfMeasurementNames;
}
