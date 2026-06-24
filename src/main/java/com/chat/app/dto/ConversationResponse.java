package com.chat.app.dto;

import java.time.LocalDateTime;

public class ConversationResponse {

    private String conversationId;

    private String userId;

    private String username;

    private String lastMessage;

    private LocalDateTime lastMessageTime;


    public ConversationResponse(
            String conversationId,
            String userId,
            String username,
            String lastMessage,
            LocalDateTime lastMessageTime
    ) {

        this.conversationId = conversationId;
        this.userId = userId;
        this.username = username;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }


    public String getConversationId() {
        return conversationId;
    }


    public String getUserId() {
        return userId;
    }


    public String getUsername() {
        return username;
    }


    public String getLastMessage() {
        return lastMessage;
    }


    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }
}