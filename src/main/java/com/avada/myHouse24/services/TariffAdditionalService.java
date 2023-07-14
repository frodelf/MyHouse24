package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Tariff;
import com.avada.myHouse24.model.AdditionalServiceForTariffDTO;

import java.util.List;

public interface TariffAdditionalService {
    List<AdditionalServiceForTariffDTO> getAllAdditionalServiceByTariff(Tariff tariff);
    void deleteAllByTariff(Tariff tariff);
//    void save(com.avada.myHouse24.entity.TariffAdditionalService tariffAdditionalService);
}
