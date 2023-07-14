package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Floor;

import java.util.List;

public interface FloorService {
    Floor getById(Long id);
    List<Floor> getAll();
    void save(Floor floor);
    void deleteById(Long id);
}
