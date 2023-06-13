package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.repo.ScoreRepository;
import com.avada.myHouse24.services.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    @Override
    public Score getById(long id) {
        return scoreRepository.findById(id).get();
    }

    @Override
    public List<Score> getAll() {
        return scoreRepository.findAll();
    }
}
