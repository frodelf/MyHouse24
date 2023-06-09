package com.avada.myHouse24.service;

import com.avada.myHouse24.entity.Score;
import com.avada.myHouse24.entity.User;

import java.util.List;

public interface ScoreService {
    Score getById(long id);
    List<Score> getAll();

}
