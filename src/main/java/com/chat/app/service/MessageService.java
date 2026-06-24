package com.chat.app.service;


import com.chat.app.entity.Message;
import com.chat.app.repository.MessageRepository;

import org.springframework.stereotype.Service;



@Service
public class MessageService {


    private final MessageRepository messageRepository;



    public MessageService(
            MessageRepository messageRepository
    ){

        this.messageRepository =
                messageRepository;

    }





    public Message markDelivered(
            String messageId
    ){



        Message message =
                messageRepository
                        .findById(messageId)
                        .orElseThrow(
                                ()->new RuntimeException(
                                        "Message not found"
                                ));



        message.setIsDelivered(true);



        return messageRepository.save(message);



    }



}