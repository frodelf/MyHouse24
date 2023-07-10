package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.model.HouseForAddDto;
import com.avada.myHouse24.model.HouseForViewDto;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

public interface HouseService {
    House getByName(String name);
    House getById(long id);
    List<House> getAll();
    List<String> getAllName();
    Page<House> getPage(int pageNumber, Model model);
    Page<HouseForViewDto> getPage(int pageNumber, Model model, List<HouseForViewDto> houseForViewDtos);
    void deleteById(long id);
    void add(HouseForAddDto house) throws IOException;
}
