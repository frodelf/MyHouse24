package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.repo.HouseRepository;
import com.avada.myHouse24.services.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseRepository houseRepository;
    @Override
    public House getByName(String name) {
        return houseRepository.findByName(name).get();
    }
}
