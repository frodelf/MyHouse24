package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.model.FlatDTO;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;

public interface FlatService {
    Flat getById(Long id);
    Flat getByNumber(int number);
    Page<Flat> getPage(int pageNumber, Model model);
    Page<FlatDTO> getPage(int pageNumber, Model model, List<FlatDTO> flatDTOS);
    void deleteById(Long id);
    void save(Flat flat);
    List<Flat> getAll();
    int getMaxId();
}
