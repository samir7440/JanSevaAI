package com.janseva.backend.service;

import com.janseva.backend.model.RefreshToken;

import com.janseva.backend.repository.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository repository;

    public RefreshToken createRefreshToken(

            String mobile

    ) {

        RefreshToken existing =
                repository.findByMobile(
                        mobile
                );

        if (existing != null) {

            repository.delete(existing);
        }

        RefreshToken token =
                new RefreshToken();

        token.setMobile(
                mobile
        );

        token.setToken(
                UUID.randomUUID().toString()
        );

        token.setExpiryDate(

                LocalDateTime.now()
                        .plusDays(7)
        );

        return repository.save(
                token
        );
    }

    public RefreshToken validateToken(

            String token

    ) {

        RefreshToken refreshToken =
                repository.findByToken(
                        token
                );

        if (refreshToken == null) {
            return null;
        }

        if (

                refreshToken
                        .getExpiryDate()

                        .isBefore(
                                LocalDateTime.now()
                        )
        ) {

            repository.delete(
                    refreshToken
            );

            return null;
        }

        return refreshToken;
    }
}