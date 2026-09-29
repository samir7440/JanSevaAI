package com.janseva.backend.controller;

import com.janseva.backend.model.RefreshToken;

import com.janseva.backend.service.JwtService;
import com.janseva.backend.service.RefreshTokenService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class RefreshTokenController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/refresh")
    public Map<String, Object> refresh(

            @RequestBody
            Map<String, String> request

    ) {

        String refreshToken =
                request.get("refreshToken");

        RefreshToken token =

                refreshTokenService
                        .validateToken(
                                refreshToken
                        );

        if (token == null) {

            return Map.of(

                    "success", false,

                    "message",
                    "Invalid refresh token"
            );
        }

        String newAccessToken =

                jwtService.generateToken(

                        token.getMobile(),

                        "CITIZEN"
                );

        return Map.of(

                "success", true,

                "token", newAccessToken
        );
    }
}