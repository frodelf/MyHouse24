package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Tariff;
import com.avada.myHouse24.model.TariffDTO;

import java.util.List;

public interface TariffService {
    List<Tariff> getAll();
    Tariff getById(long id);
    void deleteById(long id);
    void save(TariffDTO tariffDto);
}
