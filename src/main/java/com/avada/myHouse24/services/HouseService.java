package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.House;

import java.util.List;

public interface HouseService {
    House getByName(String name);
    List<House> getAll();
    List<String> getAllName();
}
