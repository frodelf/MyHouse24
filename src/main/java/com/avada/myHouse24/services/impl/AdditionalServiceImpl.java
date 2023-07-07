package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.repo.AdditionalServiceRepository;
import com.avada.myHouse24.services.AdditionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdditionalServiceImpl implements AdditionalService {
    private final AdditionalServiceRepository additionalServiceRepository;
    @Override
    public List<com.avada.myHouse24.entity.AdditionalService> getAll() {
        return additionalServiceRepository.findAll();
    }

    @Override
    public void save(com.avada.myHouse24.entity.AdditionalService additionalService) {
        additionalServiceRepository.save(additionalService);
    }

    @Override
    public void saveList(List<com.avada.myHouse24.entity.AdditionalService> additionalServices) {
        for (com.avada.myHouse24.entity.AdditionalService additionalService : additionalServices) {
            save(additionalService);
        }
    }

    @Override
    public void deleteById(long id) {
        additionalServiceRepository.deleteById(id);
    }

    @Override
    public com.avada.myHouse24.entity.AdditionalService getById(Long id) {
        return additionalServiceRepository.findById(id).get();
    }
}
