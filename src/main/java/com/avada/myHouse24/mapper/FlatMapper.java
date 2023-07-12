package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.model.FlatDTO;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.ScoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FlatMapper {
    private final HouseServiceImpl houseService;
    private final ScoreServiceImpl scoreService;
    public Flat toEntity(FlatDTO flatDTO){
        Flat flat = new Flat();
        flat.setId(flatDTO.getId());
        flat.setNumber(Math.toIntExact(flatDTO.getNumber()));
        flat.setArea(flatDTO.getArea());
        flat.setHouse(flatDTO.getHouse());
        flat.setFloor(flatDTO.getFloor());
        flat.setSection(flatDTO.getSection());
        flat.setUser(flatDTO.getUser());
        flat.setTariff(flatDTO.getTariff());
        flat.setScore(scoreService.getByNumber(flatDTO.getScoreNumber()));
        return flat;
    }
    public FlatDTO toDto(Flat flat){
        FlatDTO flatDTO = new FlatDTO();
        flatDTO.setId(flat.getId());
        flatDTO.setNumber((long) flat.getNumber());
        flatDTO.setArea(flat.getArea());
        flatDTO.setHouse(flat.getHouse());
        flatDTO.setFloor(flat.getFloor());
        flatDTO.setSection(flat.getSection());
        flatDTO.setUser(flat.getUser());
        flatDTO.setTariff(flat.getTariff());
        flatDTO.setScoreNumber(flat.getScore().getNumber());
        return flatDTO;
    }

    public List<FlatDTO> toDtoList(List<Flat> flats){
        List<FlatDTO> flatDTOS = new ArrayList<>();
        for (Flat flat : flats) {
            flatDTOS.add(toDto(flat));
        }
        return flatDTOS;
    }
}
