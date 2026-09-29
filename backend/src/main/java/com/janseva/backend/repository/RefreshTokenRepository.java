package com.janseva.backend.repository;

import com.janseva.backend.model.RefreshToken;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository
extends MongoRepository<RefreshToken, String> {

    RefreshToken findByToken(
            String token
    );

    RefreshToken findByMobile(
            String mobile
    );
}