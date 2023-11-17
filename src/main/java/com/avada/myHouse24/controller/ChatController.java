package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Chat;
import com.avada.myHouse24.enums.MessageType;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.ChatServiceImpl;
import com.avada.myHouse24.util.ImageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final AdminServiceImpl adminService;
    private final ChatServiceImpl messageService;
    private final RabbitTemplate template;


    @GetMapping("/chat/getAll/{page}/{houseId}")
    @ResponseBody
    public List<Chat> getComments (@PathVariable("page")int page, @PathVariable("houseId")long houseId){
        return messageService.getAll(page, houseId);
    }

    @GetMapping("/admin/rabbit/message")
    public ResponseEntity<String> rabbitMessage(@RequestParam("text")String message, @RequestParam("house")Long house) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Chat chat = new Chat();
        chat.setText(message);
        chat.setFromName(adminService.getAuthAdmin().getFirstName());
        chat.setFromId(house);
        chat.setIsUser(false);
        chat.setMessageType(MessageType.SMS);
        template.setExchange("common-exchange");
        template.convertAndSend(objectMapper.writeValueAsString(chat));
        return ResponseEntity.ok("Message sent to queue");
    }
    @PostMapping("/admin/rabbit/message/file")
    public ResponseEntity<String> rabbitMessage(@RequestBody MultipartFile file, @RequestParam("house")Long house) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Chat chat = new Chat();
        chat.setText(ImageUtil.fileForMessage(file));
        chat.setFromName(adminService.getAuthAdmin().getFirstName());
        chat.setFromId(house);
        chat.setIsUser(false);

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("png") || fileExtension.equalsIgnoreCase("avif") || fileExtension.equalsIgnoreCase("jfif")) {
            chat.setMessageType(MessageType.IMAGE);
        } else {
            chat.setMessageType(MessageType.FILE);
        }

        template.setExchange("common-exchange");
        template.convertAndSend(objectMapper.writeValueAsString(chat));
        return ResponseEntity.ok("Message sent to queue");
    }
    @GetMapping("/admin/message/delete/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable("id")long id){
        messageService.deleteById(id);
        return ResponseEntity.ok("Message deleted");
    }
}