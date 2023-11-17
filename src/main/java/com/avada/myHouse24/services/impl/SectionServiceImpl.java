package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Section;
import com.avada.myHouse24.repo.SectionRepository;
import com.avada.myHouse24.services.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    @Override
    public Section getByName(String name) {
        return sectionRepository.findByName(name).get();
    }
    public Section getById(Long id) {
        return sectionRepository.findById(id).get();
    }

    @Override
    public void save(Section section) {
        sectionRepository.save(section);
    }

    @Override
    public List<Section> getAll() {
        return sectionRepository.findAll();
    }

    @Override
    public long getMaxId() {
        return sectionRepository.findMaxId();
    }
    public Section getById(long id){
        return sectionRepository.findById(id).get();
    }
}
