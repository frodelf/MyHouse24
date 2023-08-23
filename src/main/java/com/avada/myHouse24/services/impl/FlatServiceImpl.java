package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.mapper.FlatMapper;
import com.avada.myHouse24.model.FlatDTO;
import com.avada.myHouse24.model.FlatForMessageDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.repo.FlatRepository;
import com.avada.myHouse24.services.FlatService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlatServiceImpl implements FlatService {
    private final FlatRepository flatRepository;

    @Override
    public Flat getById(Long id) {
        return flatRepository.findById(id).get();
    }

    @Override
    public Flat getByNumber(int number) {
        return flatRepository.findByNumber(number).get();
    }

    @Override
    public Page<Flat> getPage(int pageNumber, Model model) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int) Math.ceil(flatRepository.findAll().size() / size - 1) > 0 ? (int) Math.ceil(flatRepository.findAll().size() / size - 1) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int) size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber + 1);
        return flatRepository.findAll(pageRequest);
    }

    @Override
    public Page<FlatDTO> getPage(int pageNumber, Model model, List<FlatDTO> flatDTOS) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int) Math.ceil(flatDTOS.size() / size - 1) > 0 ? (int) Math.ceil(flatDTOS.size() / size - 1) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, flatDTOS.size());
        List<FlatDTO> pageList = flatDTOS.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<FlatDTO> userPage = new PageImpl<>(pageList, pageable, flatDTOS.size());
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber + 1);
        return userPage;
    }

    @Override
    public void deleteById(Long id) {
        flatRepository.deleteById(id);
    }

    @Override
    public void save(Flat flat) {
        flatRepository.save(flat);
    }

    @Override
    public List<Flat> getAll() {
        return flatRepository.findAll();
    }

    @Override
    public int getMaxId() {
        return Math.toIntExact(flatRepository.findMaxId());
    }

}
