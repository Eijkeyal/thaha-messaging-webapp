package com.chat.app.dto;


import java.time.LocalDateTime;


public class MessageResponse {
    private String messageId;
    private String senderId;
    private String senderUsername;
    private String content;
    private LocalDateTime createdAt;
    public MessageResponse(
            String messageId,
            String senderId,
            String senderUsername,
            String content,
            LocalDateTime createdAt
    ){

        this.messageId = messageId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.createdAt = createdAt;

    }
    public String getMessageId(){
        return messageId;
    }
    public String getSenderId(){
        return senderId;
    }
    public String getSenderUsername(){
        return senderUsername;
    }
    public String getContent(){
        return content;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

}