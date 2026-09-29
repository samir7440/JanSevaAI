package com.janseva.backend.service;

import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    // User ki language ko English mein badalne ke liye
    public String translateToEnglish(String text) {
        if (text == null) return "";
        // Yahan future mein AI API call hogi
        return text; 
    }

    // English response ko user ki language (Hindi/Urdu/Tamil) mein badalne ke liye
    public String translateFromEnglish(String text, String targetLang) {
        if (text == null) return "";
        // targetLang ke hisab se translation logic
        return text;
    }
}