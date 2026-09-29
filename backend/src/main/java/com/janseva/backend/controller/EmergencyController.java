package com.janseva.backend.controller;

import com.janseva.backend.model.Complaint;

import com.janseva.backend.repository.ComplaintRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency")
@CrossOrigin(origins = "*")
public class EmergencyController {

@Autowired
private ComplaintRepository repository;

@GetMapping("/critical")
public List<Complaint> getCriticalComplaints() {

    return repository
            .findByPriorityIgnoreCase(
                    "CRITICAL"
            );
}

@GetMapping("/high")
public List<Complaint> getHighPriorityComplaints() {

    return repository
            .findByPriorityIgnoreCase(
                    "HIGH"
            );
}


}
