package com.janseva.backend.controller;

import com.janseva.backend.model.Complaint;

import com.janseva.backend.repository.ComplaintRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {


@Autowired
private ComplaintRepository repository;

@GetMapping("/stats")
public Map<String, Object> dashboardStats() {

    List<Complaint> complaints =
            repository.findAll();

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

    long today =
            complaints.stream()

                    .filter(c ->

                            c.getCreatedAt() != null

                                    &&

                                    c.getCreatedAt()
                                            .isAfter(

                                                    LocalDateTime.now()
                                                            .minusDays(1)
                                            )
                    )

                    .count();

    Map<String, Object> response =
            new HashMap<>();

    response.put(
            "totalComplaints",
            total
    );

    response.put(
            "resolvedComplaints",
            resolved
    );

    response.put(
            "pendingComplaints",
            pending
    );

    response.put(
            "escalatedComplaints",
            escalated
    );

    response.put(
            "todayComplaints",
            today
    );

    return response;
}


}
