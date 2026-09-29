package com.janseva.backend.controller;

import com.janseva.backend.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @GetMapping("/stats")
    public Map<String, Object> getGlobalStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 1. Total Complaints kitni hain?
        stats.put("totalComplaints", complaintRepository.count());

        // 2. Pending vs Resolved (Efficiency check)
        stats.put("pending", complaintRepository.countByStatus("Pending"));
        stats.put("resolved", complaintRepository.countByStatus("Resolved"));

        // 3. Inaction Alert (Jo 24h se zyada pending hain)
        stats.put("escalatedCount", complaintRepository.countByStatus("ESCALATED_DUE_TO_INACTION"));

        // 4. Level-wise breakdown (Kaunse level par kitni bheed hai?)
        stats.put("villageLevel", complaintRepository.countByCurrentLevel("VILLAGE"));
        stats.put("districtLevel", complaintRepository.countByCurrentLevel("DISTRICT"));
        stats.put("stateLevel", complaintRepository.countByCurrentLevel("STATE"));

        return stats;
    }
}