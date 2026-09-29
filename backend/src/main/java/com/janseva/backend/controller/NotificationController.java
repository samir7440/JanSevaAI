package com.janseva.backend.controller;

import com.janseva.backend.model.Notification;

import com.janseva.backend.service.JwtService;
import com.janseva.backend.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/my")
    public List<Notification> myNotifications(

            @RequestHeader("Authorization")
            String authHeader

    ) {

        String token =
                authHeader.substring(7);

        String mobile =
                jwtService.extractMobile(
                        token
                );

        return notificationService
                .getUserNotifications(
                        mobile
                );
    }

    @PutMapping("/read/{id}")
    public Map<String, Object> markRead(

            @PathVariable String id

    ) {

        notificationService.markAsRead(
                id
        );

        return Map.of(

                "success", true,

                "message",
                "Notification marked as read"
        );
    }
}