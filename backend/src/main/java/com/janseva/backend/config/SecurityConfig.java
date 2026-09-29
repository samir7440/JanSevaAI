package com.janseva.backend.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

   // @Autowired
   // private RateLimitFilter rateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(

            HttpSecurity http

    ) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .sessionManagement(

                        session ->

                                session.sessionCreationPolicy(

                                        SessionCreationPolicy.STATELESS
                                )
                )

                .authorizeHttpRequests(

                        auth -> auth

                                .requestMatchers(

        "/api/auth/**",

        "/api/test/**",

        "/api/system/**",

        "/api/categories/**",

        "/api/track/**",

        "/api/complaint/**",
        "/api/ai-search",

        "/swagger-ui/**",

        "/v3/api-docs/**",

        "/ws/**",

        "/uploads/**"

).permitAll()

                                .requestMatchers(

                                        "/api/admin/**"

                                ).hasAuthority("ADMIN")

                                .requestMatchers(

                                        "/api/officer/**"

                                ).hasAnyAuthority(

                                        "OFFICER",

                                        "ADMIN"
                                )

                                .anyRequest()

                                .authenticated()
                )

                //.addFilterBefore(

                    //    rateLimitFilter,

                      //  UsernamePasswordAuthenticationFilter.class
               // )

                .addFilterBefore(

                        jwtFilter,

                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}