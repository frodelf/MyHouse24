package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.Tariff;
import com.avada.myHouse24.model.TariffDTO;
import com.avada.myHouse24.services.impl.TariffAdditionalServiceImpl;
import com.avada.myHouse24.services.impl.TariffServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TariffMapper {
    private final TariffAdditionalServiceImpl tariffAdditionalService;
    public TariffDTO toDto(Tariff tariff){
        TariffDTO tariffDTO = new TariffDTO();
        tariffDTO.setId(tariff.getId());
        tariffDTO.setName(tariff.getName());
        tariffDTO.setDescription(tariff.getDescription());
        tariffDTO.setAdditionalServiceForTariffDTOS(tariffAdditionalService.getAllAdditionalServiceByTariff(tariff));
        return tariffDTO;
    }

//    public Tariff toEntity(TariffDTO tariffDTO){
//        Tariff tariff = new Tariff();
//        if(tariffDTO.getId() != null)tariff.setId(tariffDTO.getId());
//        tariff.setName(tariffDTO.getName());
//        tariff.setDescription(tariffDTO.getDescription());
//
//        return tariff;
//    }
}
