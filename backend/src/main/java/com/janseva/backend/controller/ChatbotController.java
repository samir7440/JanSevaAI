package com.janseva.backend.controller;

import com.janseva.backend.service.AIService;
import com.janseva.backend.service.TranslationService;
import com.janseva.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    @Autowired
    private AIService aiService;

    @Autowired
    private TranslationService translationService; // Ab iski yellow line hat jayegi

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/ask")
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        String lang = request.getOrDefault("lang", "en"); // User ki language
        
        // 1. Semantic AI Match call
        String matchedCat = aiService.getSmartCategoryMatch(query, categoryRepository.findAll());
        
        // 2. Response ko Translate karna (Taki yellow line hate aur logic pro ban jaye)
        String baseReply = "Mujhe lagta hai aap " + matchedCat + " ke baare mein baat kar rahe hain.";
        String finalReply = translationService.translateFromEnglish(baseReply, lang);
        
        return Map.of(
            "reply", finalReply,
            "category", matchedCat
        );
    }
}