package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.repo.HouseRepository;
import com.avada.myHouse24.services.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseRepository houseRepository;
    @Override
    public House getByName(String name) {
        return houseRepository.findByName(name).get();
    }

    @Override
    public List<House> getAll() {
        return houseRepository.findAll();
    }

    @Override
    public List<String> getAllName() {
        return houseRepository.findAll().stream()
                .map(House::getName)
                .collect(Collectors.toList());
    }
}
