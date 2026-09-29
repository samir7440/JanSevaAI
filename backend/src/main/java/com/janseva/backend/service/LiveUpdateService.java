package com.janseva.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LiveUpdateService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    LiveUpdateService.class
            );

    private final SimpMessagingTemplate messagingTemplate;

    public LiveUpdateService(
            SimpMessagingTemplate messagingTemplate
    ) {

        this.messagingTemplate =
                messagingTemplate;
    }

    public void sendLiveUpdate(

            String type,

            Object data

    ) {

        Map<String, Object> payload =
                new HashMap<>();

        payload.put("type", type);

        payload.put("data", data);

        messagingTemplate.convertAndSend(
                "/topic/live",
                payload
        );

        logger.info(
                "LIVE EVENT SENT -> {}",
                type
        );
    }
}