package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.entity.UnitOfMeasurement;
import com.avada.myHouse24.model.AdditionalServiceDTO;
import com.avada.myHouse24.services.impl.UnitOfMeasurementServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdditionalServiceMapper {
    private final UnitOfMeasurementServiceImpl unitOfMeasurementService;
    public AdditionalServiceDTO toDto(AdditionalService additionalService){
        AdditionalServiceDTO additionalServiceDTO = new AdditionalServiceDTO();
        additionalServiceDTO.setId(String.valueOf(additionalService.getId()));
        additionalServiceDTO.setName(additionalService.getName());
        additionalServiceDTO.setShowInCounter(additionalService.isShowInCounter());
        additionalServiceDTO.setUnitOfMeasurementName(additionalService.getUnitOfMeasurement().getName());
        return additionalServiceDTO;
    }
    public AdditionalService toEntity(AdditionalServiceDTO additionalServiceDTO){
        AdditionalService additionalService = new AdditionalService();
        additionalService.setId(Long.valueOf(additionalServiceDTO.getId()));
        additionalService.setName(additionalServiceDTO.getName());
        additionalService.setShowInCounter(additionalServiceDTO.getShowInCounter());
        additionalService.setUnitOfMeasurement(unitOfMeasurementService.getByName(additionalServiceDTO.getUnitOfMeasurementName()));
        return additionalService;
    }
    public List<AdditionalServiceDTO> toDtoList(List<AdditionalService> additionalServices){
        List<AdditionalServiceDTO> additionalServiceDTOList = new ArrayList<>();
        for (AdditionalService additionalService : additionalServices) {
            additionalServiceDTOList.add(toDto(additionalService));
        }
        return additionalServiceDTOList;
    }
}
