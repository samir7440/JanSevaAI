package com.janseva.backend.controller;

import com.janseva.backend.model.Complaint;

import com.janseva.backend.repository.ComplaintRepository;

import com.janseva.backend.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citizen")
@CrossOrigin(origins = "*")
public class CitizenController {


@Autowired
private ComplaintRepository repository;

@Autowired
private JwtService jwtService;

@GetMapping("/my-complaints")
public List<Complaint> getMyComplaints(

        @RequestHeader("Authorization")
        String authHeader

) {

    String token =
            authHeader.substring(7);

    String mobile =
            jwtService.extractMobile(
                    token
            );

    return repository
            .findByUserMobileOrderByCreatedAtDesc(
                    mobile
            );
}

@GetMapping("/track/{complaintId}")
public Complaint trackComplaint(

        @PathVariable String complaintId

) {

    return repository
            .findByComplaintId(
                    complaintId
            );
}


}
