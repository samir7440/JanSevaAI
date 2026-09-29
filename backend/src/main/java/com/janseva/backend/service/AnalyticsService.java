package com.janseva.backend.service;

import com.janseva.backend.model.Complaint;

import com.janseva.backend.repository.ComplaintRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {


@Autowired
private ComplaintRepository complaintRepository;

public Map<String, Object> getSystemAnalytics() {

    List<Complaint> complaints =
            complaintRepository.findAll();

    long total =
            complaints.size();

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

    long emergency =
            complaints.stream()

                    .filter(c ->

                            c.getMainCategory() != null

                                    &&

                                    (

                                            c.getMainCategory()
                                                    .equalsIgnoreCase("Police")

                                                    ||

                                                    c.getMainCategory()
                                                            .equalsIgnoreCase("Health")

                                                    ||

                                                    c.getMainCategory()
                                                            .equalsIgnoreCase("Women")
                                    )
                    )

                    .count();

    long todayComplaints =
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

    Map<String, Long> categoryStats =
            new HashMap<>();

    for (Complaint complaint : complaints) {

        String category =
                complaint.getMainCategory();

        if (category == null) {
            category = "Other";
        }

        categoryStats.put(

                category,

                categoryStats.getOrDefault(
                        category,
                        0L
                ) + 1
        );
    }

    Map<String, Long> levelStats =
            new HashMap<>();

    for (Complaint complaint : complaints) {

        String level =
                complaint.getCurrentLevel();

        if (level == null) {
            level = "UNKNOWN";
        }

        levelStats.put(

                level,

                levelStats.getOrDefault(
                        level,
                        0L
                ) + 1
        );
    }

    Map<String, Object> analytics =
            new HashMap<>();

    analytics.put(
            "totalComplaints",
            total
    );

    analytics.put(
            "pendingComplaints",
            pending
    );

    analytics.put(
            "resolvedComplaints",
            resolved
    );

    analytics.put(
            "escalatedComplaints",
            escalated
    );

    analytics.put(
            "emergencyComplaints",
            emergency
    );

    analytics.put(
            "todayComplaints",
            todayComplaints
    );

    analytics.put(
            "categoryStats",
            categoryStats
    );

    analytics.put(
            "levelStats",
            levelStats
    );

    return analytics;
}


}
