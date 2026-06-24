package com.chat.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @Column(name = "conversation_id")
    private String conversationId;


    @Column(name = "type")
    private String conversationType;


    private String title;


    @Column(name = "created_at")
    private LocalDateTime createdAt;



    public String getConversationId() {
        return conversationId;
    }


    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public String getConversationType() {
        return conversationType;
    }


    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}