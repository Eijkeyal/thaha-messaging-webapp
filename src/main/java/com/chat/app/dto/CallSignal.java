package com.chat.app.dto;


public class CallSignal {


    private String conversationId;

    private String receiverId;

    private Object data;



    public String getConversationId(){

        return conversationId;

    }

    public void setConversationId(String conversationId){

        this.conversationId = conversationId;

    }
    public String getReceiverId(){

        return receiverId;

    }
    public void setReceiverId(String receiverId){

        this.receiverId = receiverId;

    }
    public Object getData(){

        return data;

    }
    public void setData(Object data){

        this.data = data;

    }
}