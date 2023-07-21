package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.CounterData;
import com.avada.myHouse24.model.CounterDataDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CounterDataMapper {
    public CounterDataDTO toDto(CounterData counterData){
        CounterDataDTO counterDataDTO = new CounterDataDTO();
        counterDataDTO.setId(counterData.getId());
        counterDataDTO.setNumber(counterData.getNumber());
        counterDataDTO.setFromDate(counterData.getFromDate());
        counterDataDTO.setStatus(counterData.getStatus());
        counterDataDTO.setData(String.valueOf(counterData.getData()));
        counterDataDTO.setFlat(counterData.getFlat());
        counterDataDTO.setAdditionalService(counterData.getAdditionalService());
        return counterDataDTO;
    }
    public CounterData toEntity(CounterDataDTO counterDataDTO) {
        CounterData counterData = new CounterData();
        counterData.setId(counterDataDTO.getId());
        counterData.setNumber(counterDataDTO.getNumber());
        counterData.setFromDate(counterDataDTO.getFromDate());
        counterData.setStatus(counterDataDTO.getStatus());
        counterData.setData(Double.parseDouble(counterDataDTO.getData()));
        counterData.setFlat(counterDataDTO.getFlat());
        counterData.setAdditionalService(counterDataDTO.getAdditionalService());
        return counterData;
    }
    public List<CounterDataDTO> toDtoList(List<CounterData> counterDataList){
        List<CounterDataDTO> counterDataDTOList = new ArrayList<>();
        for (CounterData counterData : counterDataList) {
            counterDataDTOList.add(toDto(counterData));
        }
        return counterDataDTOList;
    }
}
