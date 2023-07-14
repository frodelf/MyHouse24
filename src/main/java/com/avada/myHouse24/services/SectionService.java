package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Section;

import java.util.List;

public interface SectionService {
    Section getByName(String name);
    void save(Section section);
    List<Section> getAll();
    long getMaxId();
}
