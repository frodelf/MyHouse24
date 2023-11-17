package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Chat;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.ChatServiceImpl;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@EnableRabbit
@Component
@Log4j2
@RequiredArgsConstructor
public class RabbitMqListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final HouseServiceImpl houseService;
    private final AdminServiceImpl adminService;
    private final ChatServiceImpl chatService;
    @RabbitListener(queues = "myQueue2")
    public void processMyQueue(String string) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Chat chat = objectMapper.readValue(string, Chat.class);
        long houseId = chat.getFromId();
        chat.setFromId(adminService.getAuthAdmin().getId());
        chat.setDate(LocalDate.now());
        messagingTemplate.convertAndSend("/all/messages/"+houseId, chat);
        chat.setHouse(houseService.getById(houseId));
        chatService.save(chat);
    }
}
