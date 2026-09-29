package com.janseva.backend.controller;

import com.janseva.backend.service.AIService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIModerationController {


@Autowired
private AIService aiService;

@PostMapping("/analyze")
public Map<String, Object> analyzeComplaint(

        @RequestBody Map<String, String> request

) {

    String text =
            request.get("text");

    String imageUrl =
            request.get("imageUrl");

    return aiService.analyzeComplaint(

            text,

            imageUrl
    );
}


}
