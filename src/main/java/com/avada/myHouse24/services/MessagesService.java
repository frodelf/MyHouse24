package com.avada.myHouse24.services;

import com.avada.myHouse24.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

public interface MessagesService {
    Message findById(long id);

    List<Message> getAll();

    Page<Message> findAll(Pageable pageable);

    Message save(Message msg);
    void deleteById(long id);
}
