package com.janseva.backend.config;

import com.janseva.backend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.List;

@Component
public class JwtFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(
            JwtService jwtService
    ) {

        this.jwtService =
                jwtService;
    }

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {

        String path =
                request.getRequestURI();

        if (

        path.startsWith("/api/auth")

                ||

        path.startsWith("/api/categories")

                ||

        path.startsWith("/api/track")

                ||

        path.startsWith("/api/complaint")

                ||

        path.startsWith("/uploads")

                ||

        path.startsWith("/ws")

                ||

        path.startsWith("/swagger-ui")

                ||

        path.startsWith("/v3/api-docs")

                ||

        path.startsWith("/api/system")
               ||
        path.startsWith("/api/ai-search")

                ||

        path.startsWith("/api/test")

)
         {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String authHeader =
                request.getHeader(
                        "Authorization"
                );

        if (

                authHeader == null

                        ||

                        !authHeader.startsWith(
                                "Bearer "
                        )

        ) {

            response.setStatus(401);

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    "{ \"success\": false, \"message\": \"Unauthorized\" }"
            );

            return;
        }

        try {

            String token =
                    authHeader.substring(7);

            boolean valid =
                    jwtService.validateToken(
                            token
                    );

            if (!valid) {

                response.setStatus(401);

                response.setContentType(
                        "application/json"
                );

                response.getWriter().write(
                        "{ \"success\": false, \"message\": \"Invalid Token\" }"
                );

                return;
            }

            String mobile =
                    jwtService.extractMobile(
                            token
                    );

            String role =
                    jwtService.extractRole(
                            token
                    );

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(

                            mobile,

                            null,

                            List.of(
                                    new SimpleGrantedAuthority(role)
                            )
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(auth);

        }

        catch (Exception e) {

            SecurityContextHolder.clearContext();

            response.setStatus(401);

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    "{ \"success\": false, \"message\": \"Token Error\" }"
            );

            return;
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}