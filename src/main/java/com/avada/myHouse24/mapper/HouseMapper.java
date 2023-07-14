package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.model.HouseForViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HouseMapper {
    public HouseForViewDto toDtoForView(House house){
        HouseForViewDto houseForViewDto = new HouseForViewDto();
        houseForViewDto.setId(house.getId().toString());
        houseForViewDto.setName(house.getName());
        houseForViewDto.setAddress(house.getAddress());
        return houseForViewDto;
    }
    public List<HouseForViewDto> toDtoForViewList(List<House> house){
        List<HouseForViewDto> houseForViewDtos = new ArrayList<>();
        for (House house1 : house) {
            houseForViewDtos.add(toDtoForView(house1));
        }
        return houseForViewDtos;
    }
}
