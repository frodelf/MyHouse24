package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.UnitOfMeasurement;
import com.avada.myHouse24.model.UnitOfMeasurementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UnitOfMeasurementMapper {
    public UnitOfMeasurementDTO toDto(UnitOfMeasurement unitOfMeasurement){
        UnitOfMeasurementDTO unitOfMeasurementDTO = new UnitOfMeasurementDTO();
        unitOfMeasurementDTO.setId(unitOfMeasurement.getId().toString());
        unitOfMeasurementDTO.setName(unitOfMeasurement.getName());
        return  unitOfMeasurementDTO;
    }
    public UnitOfMeasurement toEntity(UnitOfMeasurementDTO unitOfMeasurementDTO){
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        if(unitOfMeasurementDTO.getId() != null)unitOfMeasurement.setId(Long.valueOf(unitOfMeasurementDTO.getId()));
        unitOfMeasurement.setName(unitOfMeasurementDTO.getName());
        return  unitOfMeasurement;
    }
    public List<UnitOfMeasurementDTO> toDtoList(List<UnitOfMeasurement> unitOfMeasurements){
        List<UnitOfMeasurementDTO> unitOfMeasurementDTOS = new ArrayList<>();
        for (UnitOfMeasurement unitOfMeasurement : unitOfMeasurements) {
            unitOfMeasurementDTOS.add(toDto(unitOfMeasurement));
        }
        return unitOfMeasurementDTOS;
    }
    public List<UnitOfMeasurement> toEntityList(List<UnitOfMeasurementDTO> unitOfMeasurementDTOS){
        List<UnitOfMeasurement> unitOfMeasurements = new ArrayList<>();
        for (UnitOfMeasurementDTO unitOfMeasurementDTO : unitOfMeasurementDTOS) {
            unitOfMeasurements.add(toEntity(unitOfMeasurementDTO));
        }
        return unitOfMeasurements;
    }
}
