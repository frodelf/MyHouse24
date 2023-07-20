package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.CounterData;
import com.avada.myHouse24.model.CounterDataDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.repo.CounterDataRepository;
import com.avada.myHouse24.services.CounterDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CounterDataServiceImpl implements CounterDataService {
    private final CounterDataRepository counterDataRepository;

    @Override
    public Page<CounterData> getPage(int pageNumber, Model model) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int)Math.ceil(counterDataRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(counterDataRepository.findAll().size()/size-1) : 0;
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return counterDataRepository.findAll(pageRequest);
    }

    @Override
    public Page<CounterDataDTO> getPage(int pageNumber, Model model, List<CounterDataDTO> counterDataDTOList) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int) Math.ceil(counterDataDTOList.size() / size-1) > 0 ? (int) Math.ceil(counterDataDTOList.size() / size-1) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, counterDataDTOList.size());
        List<CounterDataDTO> pageList = counterDataDTOList.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<CounterDataDTO> counterDataDTOS = new PageImpl<>(pageList, pageable, counterDataDTOList.size());
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return counterDataDTOS;
    }

    @Override
    public CounterData getById(long id) {
        return counterDataRepository.findById(id).get();
    }

    @Override
    public void save(CounterData counterData) {
        counterDataRepository.save(counterData);
    }

    @Override
    public void deleteById(long id) {
        counterDataRepository.deleteById(id);
    }
}
