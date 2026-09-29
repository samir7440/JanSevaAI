package com.janseva.backend.controller;

import com.janseva.backend.service.TranslationService;
import com.janseva.backend.service.AIService;
import com.janseva.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/voice")
@CrossOrigin(origins = "*")
public class VoiceController {

    @Autowired
    private TranslationService translationService;
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/process")
    public Map<String, Object> processVoice(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        String lang = request.get("lang");

        // 1. Translate logic
        String engText = translationService.translateToEnglish(text);
        
        // 2. AI Smart Match
        String category = aiService.getSmartCategoryMatch(engText, categoryRepository.findAll());

        // 3. Response in user's language
        String responseMessage = "Aapki samasya " + category + " vibhag ki hai.";
        String translatedResponse = translationService.translateFromEnglish(responseMessage, lang);

        return Map.of(
            "originalText", text,
            "detectedCategory", category,
            "speechResponse", translatedResponse
        );
    }
}