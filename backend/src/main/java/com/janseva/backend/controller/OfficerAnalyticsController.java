package com.janseva.backend.controller;

import com.janseva.backend.model.Complaint;

import com.janseva.backend.repository.ComplaintRepository;

import com.janseva.backend.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/officer/analytics")
@CrossOrigin(origins = "*")
public class OfficerAnalyticsController {

    @Autowired
    private ComplaintRepository repository;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public Map<String, Object> analytics(

            @RequestHeader("Authorization")
            String authHeader

    ) {

        String token =
                authHeader.substring(7);

        String mobile =
                jwtService.extractMobile(
                        token
                );

        List<Complaint> complaints =

                repository
                        .findByAssignedPersonContact(
                                mobile
                        );

        long total =
                complaints.size();

        long resolved =
                complaints.stream()

                        .filter(c ->

                                c.getStatus() != null

                                        &&

                                        c.getStatus()
                                                .equalsIgnoreCase(
                                                        "RESOLVED"
                                                )
                        )

                        .count();

        long pending =
                complaints.stream()

                        .filter(c ->

                                c.getStatus() != null

                                        &&

                                        c.getStatus()
                                                .equalsIgnoreCase(
                                                        "PENDING"
                                                )
                        )

                        .count();

        long escalated =
                complaints.stream()

                        .filter(c ->

                                c.getStatus() != null

                                        &&

                                        c.getStatus()
                                                .equalsIgnoreCase(
                                                        "ESCALATED"
                                                )
                        )

                        .count();

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "totalAssigned",
                total
        );

        response.put(
                "resolved",
                resolved
        );

        response.put(
                "pending",
                pending
        );

        response.put(
                "escalated",
                escalated
        );

        return response;
    }
}