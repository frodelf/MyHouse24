package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Floor;
import com.avada.myHouse24.repo.FloorRepository;
import com.avada.myHouse24.services.FloorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FloorServiceImpl implements FloorService {
    private final FloorRepository floorRepository;
    @Override
    public Floor getById(Long id) {
        return floorRepository.findById(id).get();
    }

    @Override
    public List<Floor> getAll() {
        return floorRepository.findAll();
    }

    @Override
    public void save(Floor floor) {
        floorRepository.save(floor);
    }

    @Override
    public void deleteById(Long id) {
        floorRepository.deleteById(id);
    }
}
