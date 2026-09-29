package com.janseva.backend.controller;

import com.janseva.backend.model.SocketMessage;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WebSocketEventController {


@Autowired
private SimpMessagingTemplate messagingTemplate;

@MessageMapping("/chat.send")
public void sendMessage(

        SocketMessage message

) {

    message.setTimestamp(
            LocalDateTime.now()
    );

    messagingTemplate.convertAndSend(

            "/topic/chat/" + message.getComplaintId(),

            message
    );
}

@MessageMapping("/notify.send")
public void sendNotification(

        SocketMessage message

) {

    message.setTimestamp(
            LocalDateTime.now()
    );

    messagingTemplate.convertAndSend(

            "/topic/notifications/" + message.getReceiver(),

            message
    );
}


}
