package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.UnitOfMeasurement;

import java.util.List;

public interface UnitOfMeasurementService {
    public List<UnitOfMeasurement> getAll();
    public void save(UnitOfMeasurement unitOfMeasurement);
    public UnitOfMeasurement getByName(String name);
}
