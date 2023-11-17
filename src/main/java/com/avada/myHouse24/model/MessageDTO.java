package com.avada.myHouse24.model;

import java.util.List;

public class MessageDTO {
    private String senderName;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String recipientsName;
    private String topic;
    private String text;
    private String date;

    public MessageDTO(String senderName, Long id, String recipients, String topic, String text, String date) {
        this.senderName = senderName;
        this.id = id;
        this.recipientsName = recipients;
        this.topic = topic;
        this.text = text;
        this.date = date;
    }
    public MessageDTO() {
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTopic() {
        return topic;
    }


    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getSenderName() {
        return senderName;
    }
    public String getRecipients() {
        return recipientsName;
    }

    public void setRecipients(String recipients) {
        this.recipientsName = recipients;
    }

}

