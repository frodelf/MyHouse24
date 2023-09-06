package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.model.ScoreDTO;
import com.avada.myHouse24.model.ScoreForFilterDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

public interface ScoreService {
    Score getById(long id);
    Score getByNumber(String number);
    List<Score> getAll();
    List<Score> getAllByStatus(String status);
    List<String> getOnlyNumber();
    boolean existNumber(String number);
    void save(Score score);
    void deleteById(Long id);
    Page<Score> getPage(int pageNumber, Model model);
    Page<ScoreDTO> getPage(int pageNumber, Model model, List<ScoreDTO> scoreDTOS);
    List<Score> forSelect(int page, int pageSize, String search);
    List<ScoreDTO> filter(ScoreForFilterDTO scoreForFilterDTO, List<ScoreDTO> scoreDTOS);
    void excel(HttpServletResponse response) throws IOException;
    List<Score> getAllEmpty();
}
