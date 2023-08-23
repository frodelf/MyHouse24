package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.model.FlatDTO;
import com.avada.myHouse24.model.FlatForMessageDTO;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.ScoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        Score score = new Score();
        try {
            score = scoreService.getByNumber(flatDTO.getScoreNumber());
        }catch (NoSuchElementException e){
            score.setNumber(flatDTO.getScoreNumber());
            scoreService.save(score);
        }
        score.setStatus("Активен");
        scoreService.save(score);
        flat.setScore(score);
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
        if(flat.getScore()!=null) {
            flatDTO.setScoreNumber(flat.getScore().getNumber());
            flatDTO.setBalance((long) flat.getScore().getBalance());
        }
        return flatDTO;
    }

    public List<FlatDTO> toDtoList(List<Flat> flats){
        List<FlatDTO> flatDTOS = new ArrayList<>();
        for (Flat flat : flats) {
            flatDTOS.add(toDto(flat));
        }
        return flatDTOS;
    }
    public FlatForMessageDTO toView(Flat flat){
        FlatForMessageDTO flatDTO = new FlatForMessageDTO(flat.getId(), flat.getNumber());
        return flatDTO;
    }
    public List<FlatForMessageDTO> toDtoForMsgList(List<Flat> flats){
        List<FlatForMessageDTO> flatDTOS = new ArrayList<>();
        for (Flat flat : flats){
            flatDTOS.add(toView(flat));
        }
        return flatDTOS;
    }
}
