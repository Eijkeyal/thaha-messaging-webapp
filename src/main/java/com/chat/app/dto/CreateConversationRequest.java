package com.chat.app.model;

public class CreateConversationRequest {

    private String targetUserId;

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }
}