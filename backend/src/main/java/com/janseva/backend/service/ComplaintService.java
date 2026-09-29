package com.janseva.backend.service;

import com.janseva.backend.model.Complaint;
import com.janseva.backend.model.ComplaintHistory;
import com.janseva.backend.model.Officer;

import com.janseva.backend.repository.ComplaintHistoryRepository;
import com.janseva.backend.repository.ComplaintRepository;
import com.janseva.backend.repository.OfficerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private OfficerRepository officerRepository;

    @Autowired
    private ComplaintHistoryRepository historyRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private LiveUpdateService liveUpdateService;

    @Autowired
    private PriorityService priorityService;

    @Autowired
    private AIService aiService;

    public Complaint submitSmartComplaint(
            Complaint complaint
    ) {

        /*
         * =========================
         * COMPLAINT ID GENERATION
         * =========================
         */

        String deptCode =
                generateDepartmentCode(
                        complaint.getMainCategory()
                );

        String uniqueNumber =
                String.valueOf(
                        System.currentTimeMillis()
                ).substring(7);

        String complaintId =

                "JS-" +

                        deptCode +

                        "-2026-" +

                        uniqueNumber;

        complaint.setComplaintId(
                complaintId
        );

        /*
         * =========================
         * INITIAL STATUS
         * =========================
         */

        complaint.setStatus(
                "PENDING"
        );

        /*
         * =========================
         * PRIORITY DETECTION
         * =========================
         */

        String priority =

                priorityService.detectPriority(

                        complaint.getMainCategory(),

                        complaint.getDescription()
                );

        complaint.setPriority(
                priority
        );

        /*
         * =========================
         * AI ANALYSIS
         * =========================
         */

         Map<String,Object> aiResult =
        aiService.analyzeComplaint(
                complaint.getDescription(),
                null
        );

boolean fakeComplaint =
        (Boolean) aiResult.getOrDefault(
                "is_fake",
                false
        );

boolean emergency =
        (Boolean) aiResult.getOrDefault(
                "emergency",
                false
        );

double confidence =
        Double.parseDouble(
                aiResult.getOrDefault(
                        "confidence",
                        95.0
                ).toString()
        );

complaint.setFakeComplaint(
        fakeComplaint
);

complaint.setAiConfidenceScore(
        confidence
);

if (emergency) {

    complaint.setPriority(
            "CRITICAL"
    );

    complaint.setStatus(
            "EMERGENCY"
    );
}
       
        /*
         * =========================
         * GOVERNANCE ROUTING
         * =========================
         */

        boolean isUrban =

        complaint.getAreaType() != null

        &&

        complaint.getAreaType()
                .equalsIgnoreCase(
                        "URBAN"
                );
        String firstLevel;

        if (isUrban) {

            complaint.setAreaType(
                    "URBAN"
            );

            complaint.setCurrentLevel(
                    "WARD"
            );

            complaint.setLevelIndex(0);

            firstLevel = "WARD";

        } else {

            complaint.setAreaType(
                    "RURAL"
            );

            complaint.setCurrentLevel(
                    "VILLAGE"
            );

            complaint.setLevelIndex(0);

            firstLevel = "VILLAGE";
        }

        /*
         * =========================
         * OFFICER ASSIGNMENT
         * =========================
         */

        Officer assignedOfficer =

                officerRepository
                        .findFirstByDepartmentIgnoreCaseAndLevelIgnoreCaseAndAvailableTrue(

                                complaint.getMainCategory(),

                                firstLevel
                        );

        if (assignedOfficer == null) {

            assignedOfficer =

                    officerRepository
                            .findFirstByDepartmentIgnoreCaseAndLevelIgnoreCaseAndAvailableTrue(

                                    "Other",

                                    firstLevel
                            );
        }

        if (assignedOfficer != null) {

            complaint.setAssignedPersonName(
                    assignedOfficer.getName()
            );

            complaint.setAssignedPersonContact(
                    assignedOfficer.getMobile()
            );

        } else {

            complaint.setAssignedPersonName(
                    "NOT_ASSIGNED"
            );

            complaint.setAssignedPersonContact(
                    "N/A"
            );
        }

        /*
         * =========================
         * OTP GENERATION
         * =========================
         */

        String otp =

                String.valueOf(

                        1000 +

                                new Random()
                                        .nextInt(9000)
                );

        complaint.setServiceOtp(
                otp
        );

        /*
         * =========================
         * DEFAULT FLAGS
         * =========================
         */

        complaint.setFieldVisited(
                false
        );

        complaint.setCreatedAt(
                LocalDateTime.now()
        );

        complaint.setUpdatedAt(
                LocalDateTime.now()
        );

        complaint.setLastEscalationAt(
                LocalDateTime.now()
        );

        /*
         * =========================
         * SAVE COMPLAINT
         * =========================
         */

        Complaint savedComplaint =
                complaintRepository.save(
                        complaint
                );

        /*
         * =========================
         * SAVE HISTORY
         * =========================
         */

        saveHistory(

                savedComplaint,

                "COMPLAINT_CREATED",

                "Complaint submitted successfully"
        );

        /*
         * =========================
         * NOTIFICATIONS
         * =========================
         */

        notificationService.sendNotification(

                savedComplaint.getUserMobile(),

                "Complaint Submitted",

                "Your complaint "

                        + savedComplaint.getComplaintId()

                        + " has been registered successfully."
        );

        notificationService.sendNotification(

                savedComplaint.getAssignedPersonContact(),

                "New Complaint Assigned",

                "Complaint "

                        + savedComplaint.getComplaintId()

                        + " assigned to you."
        );

        /*
         * =========================
         * LIVE EVENT
         * =========================
         */

        liveUpdateService.sendLiveUpdate(

                "NEW_COMPLAINT",

                savedComplaint
        );

        return savedComplaint;
    }

    /*
     * =========================
     * MANUAL ASSIGNMENT
     * =========================
     */

    public Complaint assignWork(

            String id,

            String name,

            String contact
    ) {

        Complaint complaint =

                complaintRepository
                        .findById(id)
                        .orElse(null);

        if (complaint != null) {

            complaint.setAssignedPersonName(
                    name
            );

            complaint.setAssignedPersonContact(
                    contact
            );

            complaint.setUpdatedAt(
                    LocalDateTime.now()
            );

            Complaint updated =
                    complaintRepository.save(
                            complaint
                    );

            saveHistory(

                    updated,

                    "MANUAL_ASSIGNMENT",

                    "Officer manually reassigned"
            );

            notificationService.sendNotification(

                    updated.getAssignedPersonContact(),

                    "Complaint Reassigned",

                    "Complaint "

                            + updated.getComplaintId()

                            + " has been reassigned to you."
            );

            liveUpdateService.sendLiveUpdate(

                    "COMPLAINT_REASSIGNED",

                    updated
            );

            return updated;
        }

        return null;
    }

    /*
     * =========================
     * OTP VERIFICATION
     * =========================
     */

    public boolean verifyAndResolve(

            String id,

            String otp
    ) {

        Complaint complaint =

                complaintRepository
                        .findById(id)
                        .orElse(null);

        if (

                complaint != null

                        &&

                        complaint.getServiceOtp()
                                .equals(otp)

        ) {

            complaint.setStatus(
                    "RESOLVED"
            );

            complaint.setUpdatedAt(
                    LocalDateTime.now()
            );

            complaintRepository.save(
                    complaint
            );

            saveHistory(

                    complaint,

                    "COMPLAINT_RESOLVED",

                    "Complaint resolved after OTP verification"
            );

            notificationService.sendNotification(

                    complaint.getUserMobile(),

                    "Complaint Resolved",

                    "Your complaint "

                            + complaint.getComplaintId()

                            + " has been resolved successfully."
            );

            liveUpdateService.sendLiveUpdate(

                    "COMPLAINT_RESOLVED",

                    complaint
            );

            return true;
        }

        return false;
    }

   

    private void saveHistory(

            Complaint complaint,

            String action,

            String remarks
    ) {

        ComplaintHistory history =
                new ComplaintHistory();

        history.setComplaintId(
                complaint.getComplaintId()
        );

        history.setAction(
                action
        );

        history.setLevel(
                complaint.getCurrentLevel()
        );

        history.setOfficerName(
                complaint.getAssignedPersonName()
        );

        history.setOfficerContact(
                complaint.getAssignedPersonContact()
        );

        history.setRemarks(
                remarks
        );

        history.setTimestamp(
                LocalDateTime.now()
        );

        historyRepository.save(
                history
        );
    }

    

    private String generateDepartmentCode(
            String department
    ) {

        if (department == null) {
            return "GEN";
        }

        return switch (
                department.toLowerCase()
        ) {

            case "water" -> "WTR";

            case "electricity" -> "ELC";

            case "roads" -> "RDS";

            case "health" -> "HLT";

            case "police" -> "PLC";

            case "garbage" -> "GRB";

            case "drainage" -> "DRN";

            case "internet" -> "NET";

            case "education" -> "EDU";

            case "agriculture" -> "AGR";

            case "transport" -> "TRN";

            case "housing" -> "HSG";

            case "women" -> "WMN";

            case "child" -> "CHD";

            case "employment" -> "EMP";

            default -> "GEN";
        };
    }
}