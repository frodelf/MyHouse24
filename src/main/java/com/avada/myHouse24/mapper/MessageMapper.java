package com.avada.myHouse24.mapper;

import com.avada.myHouse24.entity.Message;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.model.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    public MessageDTO toDtoForView(Message message){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setDate(message.getDate());
        if (message.getSender()!=null){
        messageDTO.setSenderName(message.getSender().getEmail());
        }
        messageDTO.setText(message.getText());
        messageDTO.setTopic(message.getTopic());
        return messageDTO;
    }
    public MessageDTO toDto(Message message){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setDate(message.getDate());
        messageDTO.setText(message.getText());
        messageDTO.setTopic(message.getTopic());
        messageDTO.setRecipients(message.getRecipientsName());
        return messageDTO;
    }
}
