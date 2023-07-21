package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.CounterData;
import com.avada.myHouse24.model.CounterDataDTO;
import com.avada.myHouse24.model.CounterDataFilterDto;
import com.avada.myHouse24.repo.CounterDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;

public interface CounterDataService {
    Page<CounterData> getPage(int pageNumber, Model model);
    Page<CounterDataDTO> getPage(int pageNumber, Model model, List<CounterDataDTO> counterDataDTOList);
    CounterData getById(long id);
    void save(CounterData counterData);
    void deleteById(long id);
    String generateRandomNumber();
    CounterDataFilterDto updateFilter(CounterDataFilterDto counterDataFilterDto);
    public boolean existNumber(String number);
    long getMaxId();

}
