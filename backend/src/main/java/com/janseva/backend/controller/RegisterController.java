package com.janseva.backend.controller;

import com.janseva.backend.dto.AuthResponse;
import com.janseva.backend.dto.RegisterRequest;

import com.janseva.backend.model.RefreshToken;
import com.janseva.backend.model.User;

import com.janseva.backend.service.JwtService;
import com.janseva.backend.service.RefreshTokenService;
import com.janseva.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public AuthResponse register(

            @RequestBody
            RegisterRequest request

    ) {

        AuthResponse response =
                new AuthResponse();

        User existing =

                userService.getUserByMobile(
                        request.getMobile()
                );

        if (existing != null) {

            response.setSuccess(false);

            response.setMessage(
                    "Mobile already registered"
            );

            return response;
        }

        User user =
                new User();

        user.setFullName(
                request.getFullName()
        );

        user.setMobile(
                request.getMobile()
        );

        user.setPassword(
                request.getPassword()
        );

        user.setRole(
                "CITIZEN"
        );

        user.setActive(
                true
        );

        user.setCreatedAt(
                LocalDateTime.now()
        );

        User saved =
                userService.createUser(
                        user
                );

        String accessToken =

                jwtService.generateToken(

                        saved.getMobile(),

                        saved.getRole()
                );

        RefreshToken refreshToken =

                refreshTokenService
                        .createRefreshToken(

                                saved.getMobile()
                        );

        response.setSuccess(
                true
        );

        response.setToken(
                accessToken
        );

        response.setRefreshToken(
                refreshToken.getToken()
        );

        response.setRole(
                saved.getRole()
        );

        response.setName(
                saved.getFullName()
        );

        response.setMobile(
                saved.getMobile()
        );

        response.setMessage(
                "Registration successful"
        );

        return response;
    }
}