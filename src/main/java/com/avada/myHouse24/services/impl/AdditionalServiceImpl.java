package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.repo.AdditionalServiceRepository;
import com.avada.myHouse24.services.AdditionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public com.avada.myHouse24.entity.AdditionalService getByName(String name) {
        return additionalServiceRepository.findByName(name).get();
    }
    public List<com.avada.myHouse24.entity.AdditionalService> forSelect(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<com.avada.myHouse24.entity.AdditionalService> additionalPage;

        if (search != null && !search.isEmpty()) {
            additionalPage = additionalServiceRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            additionalPage = additionalServiceRepository.findAll(pageable);
        }

        return additionalPage.getContent();
    }
}
