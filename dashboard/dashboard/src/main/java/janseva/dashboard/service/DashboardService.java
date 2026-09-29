package janseva.dashboard.service;

import janseva.dashboard.model.Complaint;

import janseva.dashboard.repository.ComplaintRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private ComplaintRepository repository;

    public List<Complaint> getLevelComplaints(
            String level
    ) {

        return repository.findByCurrentLevel(
                level
        );
    }

    public Map<String, Object> getStats() {

        List<Complaint> all =
                repository.findAll();

        long pending =
                all.stream()

                .filter(c ->

                        c.getStatus() != null &&

                        c.getStatus()
                                .equalsIgnoreCase(
                                        "PENDING"
                                )
                )

                .count();

        long resolved =
                all.stream()

                .filter(c ->

                        c.getStatus() != null &&

                        c.getStatus()
                                .equalsIgnoreCase(
                                        "RESOLVED"
                                )
                )

                .count();

        Map<String, Object> stats =
                new HashMap<>();

        stats.put(
                "totalComplaints",

                all.size()
        );

        stats.put(
                "pendingComplaints",

                pending
        );

        stats.put(
                "resolvedComplaints",

                resolved
        );

        stats.put(
                "waterComplaints",

                repository.countByMainCategory(
                        "Water"
                )
        );

        stats.put(
                "electricityComplaints",

                repository.countByMainCategory(
                        "Electricity"
                )
        );

        stats.put(
                "roadComplaints",

                repository.countByMainCategory(
                        "Roads"
                )
        );

        return stats;
    }
}