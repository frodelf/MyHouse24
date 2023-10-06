package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.Chat;
import com.avada.myHouse24.repo.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl {
    private final ChatRepository chatRepository;
    private final HouseServiceImpl houseService;
    public void save(Chat chat){
        chatRepository.save(chat);
    }
    public List<Chat> getAll(int page, Long houseId){
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return chatRepository.findAllByHouse(houseService.getById(houseId), pageable).getContent();
    }

//    @GetMapping
//    public Page<Product> getProducts(@RequestParam(defaultValue = "0") int page,
//                                     @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
//        return productRepository.findAll(pageable);
//    }

}
