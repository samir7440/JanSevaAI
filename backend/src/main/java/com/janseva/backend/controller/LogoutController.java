package com.janseva.backend.controller;

import com.janseva.backend.model.RefreshToken;

import com.janseva.backend.repository.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class LogoutController {

    @Autowired
    private RefreshTokenRepository repository;

    @PostMapping("/logout")
    public Map<String, Object> logout(

            @RequestBody
            Map<String, String> request

    ) {

        String refreshToken =
                request.get("refreshToken");

        RefreshToken token =
                repository.findByToken(
                        refreshToken
                );

        if (token != null) {

            repository.delete(
                    token
            );
        }

        return Map.of(

                "success", true,

                "message",
                "Logout successful"
        );
    }
}