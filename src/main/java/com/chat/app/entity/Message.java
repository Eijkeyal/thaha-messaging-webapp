package com.chat.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "sender_id")
    private String senderId;

    @Column(nullable = false)
    private String content;


    // Reply feature
    @Column(name = "reply_to_message_id")
    private String replyToMessageId;

    @Column(name = "reply_to_content")
    private String replyToContent;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_edited")
    private Boolean isEdited;

    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_delivered")
    private Boolean isDelivered;

    @Column(name = "is_seen")
    private Boolean isSeen;

    @Column(name = "seen_at")
    private LocalDateTime seenAt;


    // Getters

    public String getMessageId() {
        return messageId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public String getReplyToMessageId() {
        return replyToMessageId;
    }

    public String getReplyToContent() {
        return replyToContent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public Boolean getIsDelivered() {
        return isDelivered;
    }

    public Boolean getIsSeen() {
        return isSeen;
    }

    public LocalDateTime getSeenAt() {
        return seenAt;
    }

    // Setters

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReplyToMessageId(String replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }

    public void setReplyToContent(String replyToContent) {
        this.replyToContent = replyToContent;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }

    public void setSeenAt(LocalDateTime seenAt) {
        this.seenAt = seenAt;
    }
}