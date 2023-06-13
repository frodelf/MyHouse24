package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Score;

import java.util.List;

public interface ScoreService {
    Score getById(long id);
    List<Score> getAll();

}
