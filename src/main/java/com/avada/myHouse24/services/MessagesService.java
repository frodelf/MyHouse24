package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MessagesService {
    Message findById(long id);
    List<Message> findAll();
    Message save(Message msg);
    void deleteById(long id);
}
