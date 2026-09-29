package com.janseva.backend.controller;

import com.janseva.backend.dto.ComplaintRequest;
import com.janseva.backend.model.Complaint;

import com.janseva.backend.repository.ComplaintRepository;

import com.janseva.backend.service.AIService;
import com.janseva.backend.service.ComplaintService;
import com.janseva.backend.service.SecurityService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private AIService aiService;

    @Autowired
    private SecurityService securityService;

    /*
     * =========================================
     * SUBMIT SMART COMPLAINT
     * =========================================
     */

    @PostMapping("/complaint")
    public ResponseEntity<Complaint> submitComplaint(

            @RequestBody ComplaintRequest request

    ) {

        Complaint complaint =
                new Complaint();

        /*
         * =========================
         * BASIC DETAILS
         * =========================
         */

        complaint.setMainCategory(
                request.getMainCategory()
        );

        complaint.setSubCategory(
                request.getSubCategory()
        );

        complaint.setDescription(
                request.getDescription()
        );

        complaint.setUserMobile(
                request.getUserMobile()
        );

        /*
         * =========================
         * LOCATION DETAILS
         * =========================
         */

        complaint.setLatitude(
                request.getLatitude()
        );

        complaint.setLongitude(
                request.getLongitude()
        );

        /*
         * =========================
         * GOVERNANCE DETAILS
         * =========================
         */

        complaint.setAreaType(
                request.getAreaType()
        );

        complaint.setVillage(
                request.getVillage()
        );

        complaint.setBlock(
                request.getBlock()
        );

        complaint.setDistrict(
                request.getDistrict()
        );

        complaint.setState(
                request.getState()
        );

        complaint.setCity(
                request.getCity()
        );

        complaint.setWard(
                request.getWard()
        );

        /*
         * =========================
         * SAVE COMPLAINT
         * =========================
         */

        Complaint savedComplaint =

                complaintService
                        .submitSmartComplaint(
                                complaint
                        );

        return new ResponseEntity<>(

                savedComplaint,

                HttpStatus.CREATED
        );
    }

    /*
     * =========================================
     * OFFICER COMPLAINT VIEW
     * =========================================
     */

    @GetMapping("/officer/complaints")
    public ResponseEntity<List<Complaint>>
    getComplaintsForOfficer(

            @RequestParam(
                    defaultValue = "OFFICER"
            )

            String role

    ) {

        List<Complaint> complaints =
                complaintRepository.findAll();

        if (

                "OFFICER".equalsIgnoreCase(
                        role
                )

        ) {

            List<Complaint> maskedList =

                    complaints.stream()

                            .map(c -> {

                                if (

                                        c.getUserMobile()
                                                != null

                                ) {

                                    c.setUserMobile(

                                            securityService
                                                    .maskMobile(

                                                            c.getUserMobile()
                                                    )
                                    );
                                }

                                return c;

                            })

                            .collect(
                                    Collectors.toList()
                            );

            return ResponseEntity.ok(
                    maskedList
            );
        }

        return ResponseEntity.ok(
                complaints
        );
    }

    /*
     * =========================================
     * ADMIN ALL DATA
     * =========================================
     */

    @GetMapping("/admin/all")
    public List<Complaint> getAllData() {

        return complaintRepository.findAll();
    }

    /*
     * =========================================
     * TRACK USER COMPLAINTS
     * =========================================
     */

    @GetMapping("/track/{mobile}")
    public List<Complaint> trackComplaint(

            @PathVariable String mobile

    ) {

        return complaintRepository
                .findByUserMobile(
                        mobile
                );
    }

    /*
     * =========================================
     * AI MEDIA ANALYSIS
     * =========================================
     */

    @PostMapping("/complaint/analyze-media")
    public ResponseEntity<Map<String, Object>>
    analyzeMedia(

            @RequestParam String imageUrl,

            @RequestParam String text

    ) {

        Map<String, Object> result =

                aiService.analyzeComplaint(
                        text,
                        imageUrl
                );

        if (

                result.get("is_fake")
                        != null

                        &&

                        (boolean)
                                result.get("is_fake")

        ) {

            return ResponseEntity

                    .status(
                            HttpStatus.FORBIDDEN
                    )

                    .body(result);
        }

        return ResponseEntity.ok(
                result
        );
    }

    /*
     * =========================================
     * MANUAL ASSIGNMENT
     * =========================================
     */

    @PutMapping("/assign/{id}")
    public ResponseEntity<Complaint>
    assignToPerson(

            @PathVariable String id,

            @RequestParam String name,

            @RequestParam String contact

    ) {

        Complaint updated =

                complaintService.assignWork(

                        id,

                        name,

                        contact
                );

        if (updated != null) {

            return ResponseEntity.ok(
                    updated
            );
        }

        return ResponseEntity

                .notFound()

                .build();
    }

    /*
     * =========================================
     * OTP VERIFICATION
     * =========================================
     */

    @PostMapping("/verify-close")
    public ResponseEntity<String>
    finalVerification(

            @RequestParam String id,

            @RequestParam String otp

    ) {

        boolean verified =

                complaintService
                        .verifyAndResolve(

                                id,

                                otp
                        );

        if (verified) {

            return ResponseEntity.ok(

                    "Verification Successful! " +

                    "Issue marked as Resolved."
            );
        }

        return ResponseEntity

                .status(
                        HttpStatus.UNAUTHORIZED
                )

                .body(

                        "Invalid OTP. " +

                        "Work cannot be closed."
                );
    }
}