package com.janseva.backend.controller;

import com.janseva.backend.model.Message;

import com.janseva.backend.repository.MessageRepository;

import com.janseva.backend.service.LiveUpdateService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {


@Autowired
private MessageRepository repository;

@Autowired
private LiveUpdateService liveUpdateService;

@PostMapping("/send")
public Map<String, Object> sendMessage(

        @RequestBody Message message

) {

    message.setTimestamp(
            LocalDateTime.now()
    );

    Message saved =
            repository.save(
                    message
            );

    liveUpdateService.sendLiveUpdate(

            "NEW_CHAT_MESSAGE",

            saved
    );

    return Map.of(

            "success", true,

            "message",
            "Message sent successfully"
    );
}

@GetMapping("/{complaintId}")
public List<Message> getMessages(

        @PathVariable String complaintId

) {

    return repository
            .findByComplaintIdOrderByTimestampAsc(
                    complaintId
            );
}


}
