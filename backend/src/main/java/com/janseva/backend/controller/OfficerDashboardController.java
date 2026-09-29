package com.janseva.backend.controller;

import com.janseva.backend.model.Complaint;

import com.janseva.backend.repository.ComplaintRepository;

import com.janseva.backend.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officer")
@CrossOrigin(origins = "*")
public class OfficerDashboardController {


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
            .findByAssignedPersonContact(
                    mobile
            );
}

@GetMapping("/pending")
public List<Complaint> pendingComplaints(

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
            .findByAssignedPersonContactAndStatusIgnoreCase(

                    mobile,

                    "PENDING"
            );
}

@GetMapping("/escalated")
public List<Complaint> escalatedComplaints(

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
            .findByAssignedPersonContactAndStatusIgnoreCase(

                    mobile,

                    "ESCALATED"
            );
}


}
