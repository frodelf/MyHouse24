package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.entity.Tariff;
import com.avada.myHouse24.model.AdditionalServiceForTariffDTO;
import com.avada.myHouse24.repo.TariffAdditionalServiceRepository;
import com.avada.myHouse24.services.TariffAdditionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TariffAdditionalServiceImpl implements TariffAdditionalService {
    private final TariffAdditionalServiceRepository tariffAdditionalServiceRepository;
    @Override
    public List<AdditionalServiceForTariffDTO> getAllAdditionalServiceByTariff(Tariff tariff) {
        List<AdditionalServiceForTariffDTO> additionalServiceForTariffDTOS = new ArrayList<>();
        List<com.avada.myHouse24.entity.TariffAdditionalService> tariffAdditionalServices = tariffAdditionalServiceRepository.findAllByTariff(tariff);
        for (com.avada.myHouse24.entity.TariffAdditionalService tariffAdditionalService : tariffAdditionalServices) {
            additionalServiceForTariffDTOS.add(new AdditionalServiceForTariffDTO(tariffAdditionalService.getAdditionalService(), tariffAdditionalService.getPrice()));
        }
        return additionalServiceForTariffDTOS;
    }

    @Override
    public void deleteAllByTariff(Tariff tariff) {
        tariffAdditionalServiceRepository.deleteAllByTariff(tariff);
    }
}
