package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.model.ScoreDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;

public interface ScoreService {
    Score getById(long id);
    Score getByNumber(String number);
    List<Score> getAll();
    List<String> getOnlyNumber();
    boolean existNumber(String number);
    void save(Score score);
    void deleteById(Long id);
    Page<Score> getPage(int pageNumber, Model model);
    Page<ScoreDTO> getPage(int pageNumber, Model model, List<ScoreDTO> scoreDTOS);
}
