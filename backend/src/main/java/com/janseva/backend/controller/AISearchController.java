package com.janseva.backend.controller;

import com.janseva.backend.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AISearchController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ai-search")
    public Map<String, Object> aiSearch(@RequestBody Map<String, String> request) {

        String query = request.get("query");

        // AI analysis
        Map<String, Object> result =
                aiService.analyzeComplaint(query, null);

        String response =
                "PrajaSetu AI Analysis:\n\n" +
                "Issue detected regarding: " + query +
                "\n\nDepartment Assigned Successfully.";

        return Map.of(
                "success", true,
                "answer", response,
                "aiResult", result
        );
    }
}