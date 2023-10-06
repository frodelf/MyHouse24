package com.avada.myHouse24.controller;

import com.avada.myHouse24.entity.Message;
import com.avada.myHouse24.model.HouseForViewDto;
import com.avada.myHouse24.services.impl.AdminServiceImpl;
import com.avada.myHouse24.services.impl.HouseServiceImpl;
import com.avada.myHouse24.services.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final AdminServiceImpl adminService;
    private final MessageServiceImpl messageService;
    private final HouseServiceImpl houseService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/application")
    public void send(final Message message) throws Exception {
        Message res = new Message();
        res.setDate(LocalDate.now());
        res.setFromName(adminService.getAuthAdmin().getFirstName());
        res.setIsUser(false);
        res.setFromId(adminService.getAuthAdmin().getId());
        res.setHouse(houseService.getById(message.getFromId()));
        res.setText(message.getText());
        messageService.save(res);
        res.setHouse(null);
        messagingTemplate.convertAndSend("/all/messages/"+message.getFromId(), res);
    }

    @GetMapping("/message/getAll/{page}/{houseId}")
    @ResponseBody
    public List<Message> getComments (@PathVariable("page")int page, @PathVariable("houseId")long houseId){
        return messageService.getAll(page, houseId);
    }
}