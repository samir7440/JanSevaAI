package com.janseva.backend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {



private static final String SECRET =

        "JANSEVA_SUPER_SECRET_KEY_2026_FOR_REAL_SYSTEM_SECURITY";



private final Key key =

        Keys.hmacShaKeyFor(

                SECRET.getBytes(
                        StandardCharsets.UTF_8
                )
        );



public String generateToken(

        String mobile,

        String role

) {

    return Jwts.builder()

            .subject(mobile)

            .claim(
                    "role",
                    role
            )

            .issuedAt(
                    new Date()
            )

            .expiration(

                    new Date(

                            System.currentTimeMillis()

                                    + (1000L * 60 * 60 * 24)
                    )
            )

            .signWith(key)

            .compact();
}



public String extractMobile(
        String token
) {

    return Jwts.parser()

            .verifyWith(
                    (javax.crypto.SecretKey) key
            )

            .build()

            .parseSignedClaims(token)

            .getPayload()

            .getSubject();
}



public String extractRole(
        String token
) {

    return Jwts.parser()

            .verifyWith(
                    (javax.crypto.SecretKey) key
            )

            .build()

            .parseSignedClaims(token)

            .getPayload()

            .get(

                    "role",

                    String.class
            );
}



public boolean validateToken(
        String token
) {

    try {

        Jwts.parser()

                .verifyWith(
                        (javax.crypto.SecretKey) key
                )

                .build()

                .parseSignedClaims(token);

        return true;

    } catch (Exception e) {

        return false;
    }
}


}
