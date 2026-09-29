package com.janseva.backend.controller;

import com.janseva.backend.dto.AuthResponse;
import com.janseva.backend.dto.LoginRequest;

import com.janseva.backend.model.RefreshToken;
import com.janseva.backend.model.User;

import com.janseva.backend.service.JwtService;
import com.janseva.backend.service.RefreshTokenService;
import com.janseva.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public AuthResponse login(

            @RequestBody LoginRequest request

    ) {

        User user =

                userService.login(

                        request.getMobile(),

                        request.getPassword()
                );

        AuthResponse response =
                new AuthResponse();

        if (user == null) {

            response.setSuccess(
                    false
            );

            response.setMessage(
                    "Invalid mobile or password"
            );

            return response;
        }

        String accessToken =

                jwtService.generateToken(

                        user.getMobile(),

                        user.getRole()
                );

        RefreshToken refreshToken =

                refreshTokenService
                        .createRefreshToken(

                                user.getMobile()
                        );

        response.setSuccess(
                true
        );

        response.setToken(
                accessToken
        );

        response.setRole(
                user.getRole()
        );

        response.setName(
                user.getFullName()
        );

        response.setMobile(
                user.getMobile()
        );

        response.setMessage(
                refreshToken.getToken()
        );

        return response;
    }
}