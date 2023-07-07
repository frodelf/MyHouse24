package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.UnitOfMeasurement;

import java.util.List;

public interface UnitOfMeasurementService {
    List<UnitOfMeasurement> getAll();
    void save(UnitOfMeasurement unitOfMeasurement);
    void deleteById(long id);
    UnitOfMeasurement getByName(String name);
    void saveList(List<UnitOfMeasurement> unitOfMeasurement);
}
