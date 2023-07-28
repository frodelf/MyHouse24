package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Flat;
import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.model.ScoreDTO;
import com.avada.myHouse24.model.UserForViewDTO;
import com.avada.myHouse24.repo.ScoreRepository;
import com.avada.myHouse24.services.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final AccountTransactionServiceImpl accountTransactionService;
    private final FlatServiceImpl flatService;
    @Override
    public Score getById(long id) {
        return scoreRepository.findById(id).get();
    }

    @Override
    public List<Score> getAll() {

        return scoreRepository.findAll();
    }

    @Override
    public List<Score> getAllByStatus(String status) {
        return scoreRepository.findAllByStatus(status);
    }

    @Override
    public List<String> getOnlyNumber() {
        List<Score> scores = scoreRepository.findAll();
        List<String> numbers = new ArrayList<>();
        for (Score score : scores) {
            numbers.add(score.getNumber());
        }
        return numbers;
    }
    @Override
    public boolean existNumber(String number){
        return scoreRepository.existsByNumber(number);
    }

    @Override
    public void save(Score score) {
        scoreRepository.save(score);
    }

    @Override
    public void deleteById(Long id) {
        if(getById(id).getFlat() != null) {
            Flat flat = getById(id).getFlat();
            flat.setScore(null);
            flatService.save(flat);
        }
        accountTransactionService.clearAllScore(getById(id));
        scoreRepository.deleteById(id);
    }
    @Override
    public Score getByNumber(String number) {
        return scoreRepository.findByNumber(number).get();
    }



    @Override
    public Page<Score> getPage(int pageNumber, Model model) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int)Math.ceil(scoreRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(scoreRepository.findAll().size()/size-1) : 0;
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return scoreRepository.findAll(pageRequest);
    }

    @Override
    public Page<ScoreDTO> getPage(int pageNumber, Model model, List<ScoreDTO> scoreDTOS) {
        pageNumber = pageNumber - 1;
        double size = 10.0;
        int max = (int) Math.ceil(scoreDTOS.size() / size-1) > 0 ? (int) Math.ceil(scoreDTOS.size() / size-1) : 0;
        if (pageNumber < 0) pageNumber = 0;
        if (pageNumber > max) pageNumber = max;
        int startIndex = pageNumber * (int) size;
        int endIndex = Math.min(startIndex + (int) size, scoreDTOS.size());
        List<ScoreDTO> pageList = scoreDTOS.subList(startIndex, endIndex);
        Pageable pageable = PageRequest.of(pageNumber, (int) size);
        Page<ScoreDTO> scoreDTOPage = new PageImpl<>(pageList, pageable, scoreDTOS.size());
        model.addAttribute("max", max);
        model.addAttribute("current", pageNumber+1);
        return scoreDTOPage;
    }

    public List<Score> forSelect(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Score> scorePage;

        if (search != null && !search.isEmpty()) {
            scorePage = scoreRepository.findByNumberContainingIgnoreCase(search, pageable);
        } else {
            scorePage = scoreRepository.findAll(pageable);
        }

        return scorePage.getContent();
    }
}
