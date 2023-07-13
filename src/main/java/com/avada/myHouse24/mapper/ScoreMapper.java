package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.model.ScoreDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScoreMapper {
    public ScoreDTO toDto(Score score){
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setId(score.getId());
        scoreDTO.setNumber(score.getNumber());
        scoreDTO.setStatus(score.getStatus());
        scoreDTO.setHouse(score.getFlat().getHouse());
        scoreDTO.setSection(score.getFlat().getSection());
        scoreDTO.setFlat(score.getFlat());
        return scoreDTO;
    }
    public Score toEntity(ScoreDTO scoreDTO){
        Score score = new Score();
        score.setId(scoreDTO.getId());
        score.setNumber(scoreDTO.getNumber());
        score.setStatus(scoreDTO.getStatus());
        score.setFlat(score.getFlat());
        return score;
    }

    public List<ScoreDTO> toDtoList(List<Score> scores){
        List<ScoreDTO> scoreDTOS = new ArrayList<>();
        for (Score score : scores) {
            scoreDTOS.add(toDto(score));
        }
        return scoreDTOS;
    }
}
