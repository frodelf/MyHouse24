package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Tariff;
import com.avada.myHouse24.entity.TariffAdditionalService;
import com.avada.myHouse24.model.AdditionalServiceForTariffDTO;
import com.avada.myHouse24.model.TariffDTO;
import com.avada.myHouse24.repo.TariffRepository;
import com.avada.myHouse24.services.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {
    private final TariffRepository tariffRepository;
    @Override
    public List<Tariff> getAll() {
        return tariffRepository.findAll();
    }

    @Override
    public Tariff getById(long id) {
        return tariffRepository.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        tariffRepository.deleteById(id);
    }

    @Override
    public void save(TariffDTO tariffDto) {
        Tariff tariff = new Tariff();
        if(tariffDto.getId() != null)tariff.setId(tariffDto.getId());
        tariff.setName(tariffDto.getName());
        tariff.setDescription(tariffDto.getDescription());
        tariff.setDateEdit(Date.valueOf(LocalDate.now()));
        tariff.setTariffAdditionalService(tariffDto.getAdditionalServiceForTariffDTOS());
        tariffRepository.save(tariff);
    }
}
