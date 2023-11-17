package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.UnitOfMeasurement;
import com.avada.myHouse24.repo.AdditionalServiceRepository;
import com.avada.myHouse24.repo.UnitOfMeasurementRepository;
import com.avada.myHouse24.services.UnitOfMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UnitOfMeasurementServiceImpl implements UnitOfMeasurementService {
    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    private final AdditionalServiceRepository additionalServiceRepository;

    @Override
    public List<UnitOfMeasurement> getAll() {
        return unitOfMeasurementRepository.findAll();
    }

    @Override
    public void save(UnitOfMeasurement unitOfMeasurement) {
        unitOfMeasurementRepository.save(unitOfMeasurement);
    }
    @Override
    public void saveList(List<UnitOfMeasurement> unitOfMeasurement) {
        for (UnitOfMeasurement measurement : unitOfMeasurement) {
            save(measurement);
        }
    }
    @Override
    public void deleteById(long id) {
        if(isUses(id))return;
        unitOfMeasurementRepository.deleteById(id);
    }
    public boolean isUses(Long id){
        if(additionalServiceRepository.existsByUnitOfMeasurement(unitOfMeasurementRepository.findById(id).get()))return true;
        return false;
    }
    @Override
    public UnitOfMeasurement getByName(String name) {
        return unitOfMeasurementRepository.findByName(name).get();
    }
}
