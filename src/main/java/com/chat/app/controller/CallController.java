package com.chat.app.controller;

import com.chat.app.dto.CallSignal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CallController {

    private final SimpMessagingTemplate template;
    public CallController(
            SimpMessagingTemplate template
    ){
        this.template = template;
    }

    @MessageMapping("/call")
    public void call(
            CallSignal signal
    ){
        template.convertAndSendToUser(

                signal.getReceiverId(),

                "/queue/call",

                signal.getData()

        );


    }


}