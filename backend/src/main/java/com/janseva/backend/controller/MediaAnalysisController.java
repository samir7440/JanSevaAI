package com.janseva.backend.controller;

import com.janseva.backend.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaAnalysisController {

    @Autowired
    private AIService aiService;

    @PostMapping("/analyze-incident")
    public ResponseEntity<Map<String, Object>> analyzeMedia(@RequestBody Map<String, String> request) {
        String fileUrl = request.get("fileUrl");
        String description = request.get("description");

        // AI Service ko bhejenge jo Video/Image frames ko analyze karega
        // Isme Fake Detection logic (Metadata + Content Analysis) shamil hai
        Map<String, Object> analysisResult = aiService.analyzeComplaint(description, fileUrl);

        // Agar AI ko lagta hai video purana hai ya edited hai (Fake Detection)
        if (analysisResult.get("is_fake") != null && (boolean) analysisResult.get("is_fake")) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Fake Media Detected! Satellite coordinates do not match.",
                "status", "REJECTED"
            ));
        }

        return ResponseEntity.ok(analysisResult);
    }
}
