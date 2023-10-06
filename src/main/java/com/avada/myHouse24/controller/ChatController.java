package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Chat;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final AdminServiceImpl adminService;
    private final ChatServiceImpl messageService;
    private final HouseServiceImpl houseService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/application")
    public void send(final Chat chat) throws Exception {
        Chat res = new Chat();
        res.setDate(LocalDate.now());
        res.setFromName(adminService.getAuthAdmin().getFirstName());
        res.setIsUser(false);
        res.setFromId(adminService.getAuthAdmin().getId());
        res.setHouse(houseService.getById(chat.getFromId()));
        res.setText(chat.getText());
        messageService.save(res);
        res.setHouse(null);
        messagingTemplate.convertAndSend("/all/messages/"+ chat.getFromId(), res);
    }

    @GetMapping("/chat/getAll/{page}/{houseId}")
    @ResponseBody
    public List<Chat> getComments (@PathVariable("page")int page, @PathVariable("houseId")long houseId){
        return messageService.getAll(page, houseId);
    }
}