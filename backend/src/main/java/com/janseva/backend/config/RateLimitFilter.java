package com.janseva.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter
        extends OncePerRequestFilter {

    private final Map<String, Long> requestMap =
            new ConcurrentHashMap<>();

    private static final long LIMIT_TIME =
           0;

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {

        String ip =
                request.getRemoteAddr();

        long currentTime =
                System.currentTimeMillis();

        Long lastRequest =
                requestMap.get(ip);

        if (

                lastRequest != null

                        &&

                        currentTime - lastRequest
                                < LIMIT_TIME

        ) {

            response.setContentType(
                    "application/json"
            );

            response.setStatus(429);

            response.getWriter().write(
                    "{ \"success\": false, \"message\": \"Too many requests\" }"
            );

            return;
        }

        requestMap.put(
                ip,
                currentTime
        );

        Iterator<Map.Entry<String, Long>> iterator =
                requestMap.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<String, Long> entry =
                    iterator.next();

            if (

                    currentTime
                            - entry.getValue()
                            > 60000

            ) {

                iterator.remove();
            }
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}