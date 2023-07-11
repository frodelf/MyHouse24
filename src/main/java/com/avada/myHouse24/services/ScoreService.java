package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Score;

import java.util.List;

public interface ScoreService {
    Score getById(long id);
    Score getByNumber(String number);
    List<Score> getAll();
    List<String> getOnlyNumber();
    boolean existNumber(String number);
    void save(Score score);
    void deleteById(Long id);
}
