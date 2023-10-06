package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.Message;
import com.avada.myHouse24.repo.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl {
    private final MessageRepository messageRepository;
    private final HouseServiceImpl houseService;
    public void save(Message message){
        messageRepository.save(message);
    }
    public List<Message> getAll(int page, Long houseId){
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return messageRepository.findAllByHouse(houseService.getById(houseId), pageable).getContent();
    }

//    @GetMapping
//    public Page<Product> getProducts(@RequestParam(defaultValue = "0") int page,
//                                     @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
//        return productRepository.findAll(pageable);
//    }

}
