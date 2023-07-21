package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.CounterData;
import com.avada.myHouse24.mapper.CounterDataMapper;
import com.avada.myHouse24.model.CounterDataDTO;
import com.avada.myHouse24.model.CounterDataFilterDto;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CounterDataServiceImpl implements CounterDataService {
    private final CounterDataRepository counterDataRepository;
    private final CounterDataMapper counterDataMapper;
    private final SectionServiceImpl sectionService;
    private final FlatServiceImpl flatService;
    private final HouseServiceImpl houseService;
    private final AdditionalServiceImpl additionalService;

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
    public long getMaxId(){
        return counterDataRepository.findMaxId();
    }
    public CounterDataFilterDto updateFilter(CounterDataFilterDto counterDataFilterDto){
        if(counterDataFilterDto.getHouse() != null)counterDataFilterDto.setHouseName(houseService.getById(counterDataFilterDto.getHouse()).getName());
        if(counterDataFilterDto.getSection() != null)counterDataFilterDto.setSectionName(sectionService.getById(counterDataFilterDto.getSection()).getName());
        if(counterDataFilterDto.getFlat() != null)counterDataFilterDto.setFlatName(String.valueOf(flatService.getById(counterDataFilterDto.getFlat()).getNumber()));
        if(counterDataFilterDto.getAdditionalService() != null)counterDataFilterDto.setAdditionalServiceName(additionalService.getById(counterDataFilterDto.getAdditionalService()).getName());
        return counterDataFilterDto;
    }
    public String generateRandomNumber() {
        Random random = new Random();
        long min = 1000000000L;
        long max = 9999999999L;
        return String.valueOf(min + ((long) (random.nextDouble() * (max - min + 1))));
    }
    public boolean existNumber(String number){
        return counterDataRepository.existsByNumber(number);
    }
    public List<CounterDataDTO> filter(CounterDataFilterDto filter){
        List<CounterDataDTO> result = counterDataMapper.toDtoList(counterDataRepository.findAll());
        if(filter.getHouse() != null){
            result = result.stream()
                    .filter(dto -> dto.getFlat() != null && dto.getFlat().getHouse().getId().toString().contains(filter.getHouse().toString()))
                    .collect(Collectors.toList());
        }
        if(filter.getSection() != null){
            result = result.stream()
                    .filter(dto -> dto.getFlat() != null && dto.getFlat().getSection().getId().toString().contains(filter.getSection().toString()))
                    .collect(Collectors.toList());
        }
        if(filter.getFlat() != null){
            result = result.stream()
                    .filter(dto -> dto.getFlat() != null && dto.getFlat().getId().toString().contains(filter.getFlat().toString()))
                    .collect(Collectors.toList());
        }
        if(filter.getAdditionalService() != null){
            result = result.stream()
                    .filter(dto -> dto.getAdditionalService() != null && dto.getAdditionalService().getId().toString().contains(filter.getAdditionalService().toString()))
                    .collect(Collectors.toList());
        }
        return result;
    }
    public List<CounterDataDTO> filter(CounterDataFilterDto filter, String status, String number, Date date, List<CounterData> result){
        if(!status.isBlank()){
            result = result.stream()
                    .filter(dto -> dto.getStatus() != null && dto.getStatus().contains(status))
                    .collect(Collectors.toList());
        }
        if(!number.isBlank()){
            result = result.stream()
                    .filter(dto -> dto.getNumber() != null && dto.getNumber().contains(number))
                    .collect(Collectors.toList());
        }
        if(!Objects.equals(date.toString(), "1000-01-01")){
            result = result.stream()
                    .filter(dto -> dto.getFromDate() != null && dto.getFromDate() == date)
                    .collect(Collectors.toList());
        }
        if(filter.getHouse() != null){
            result = result.stream()
                    .filter(dto -> dto.getFlat() != null && dto.getFlat().getHouse().getId().toString().contains(filter.getHouse().toString()))
                    .collect(Collectors.toList());
        }
        if(filter.getSection() != null){
            result = result.stream()
                    .filter(dto -> dto.getFlat() != null && dto.getFlat().getSection().getId().toString().contains(filter.getSection().toString()))
                    .collect(Collectors.toList());
        }
        if(filter.getFlat() != null){
            result = result.stream()
                    .filter(dto -> dto.getFlat() != null && dto.getFlat().getId().toString().contains(filter.getFlat().toString()))
                    .collect(Collectors.toList());
        }
        if(filter.getAdditionalService() != null){
            result = result.stream()
                    .filter(dto -> dto.getAdditionalService() != null && dto.getAdditionalService().getId().toString().contains(filter.getAdditionalService().toString()))
                    .collect(Collectors.toList());
        }
        return counterDataMapper.toDtoList(result);
    }

}
