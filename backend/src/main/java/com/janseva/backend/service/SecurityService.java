package com.janseva.backend.service;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    
    public String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 10) {
            return "XXXXXXXXXX";
        }
        return "XXXXXX" + mobile.substring(mobile.length() - 4);
    }

    /**
     * User ka naam mask karne ke liye (Optional)
     * Input: Samir Sulakhe -> Output: S**** S******
     */
    public String maskName(String name) {
        if (name == null || name.isEmpty()) {
            return "Anonymous User";
        }
        String[] parts = name.split(" ");
        StringBuilder maskedName = new StringBuilder();
        
        for (String part : parts) {
            if (part.length() > 0) {
                maskedName.append(part.charAt(0))
                          .append("*".repeat(part.length() - 1))
                          .append(" ");
            }
        }
        return maskedName.toString().trim();
    }
}