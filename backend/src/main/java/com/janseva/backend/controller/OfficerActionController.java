package com.janseva.backend.controller;

import com.janseva.backend.model.Complaint;
import com.janseva.backend.model.ComplaintHistory;

import com.janseva.backend.repository.ComplaintHistoryRepository;
import com.janseva.backend.repository.ComplaintRepository;

import com.janseva.backend.service.LiveUpdateService;
import com.janseva.backend.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/officer")
@CrossOrigin(origins = "*")
public class OfficerActionController {


@Autowired
private ComplaintRepository complaintRepository;

@Autowired
private ComplaintHistoryRepository historyRepository;

@Autowired
private NotificationService notificationService;

@Autowired
private LiveUpdateService liveUpdateService;

@PostMapping("/update-status/{complaintId}")
public Map<String, Object> updateComplaintStatus(

        @PathVariable String complaintId,

        @RequestBody Map<String, String> request

) {

    Complaint complaint =
            complaintRepository.findByComplaintId(
                    complaintId
            );

    if (complaint == null) {

        return Map.of(

                "success", false,

                "message", "Complaint not found"
        );
    }

    String newStatus =
            request.get("status");

    String remarks =
            request.get("remarks");

    complaint.setStatus(
            newStatus
    );

    complaintRepository.save(
            complaint
    );

    ComplaintHistory history =
            new ComplaintHistory();

    history.setComplaintId(
            complaint.getComplaintId()
    );

    history.setAction(
            "STATUS_UPDATED"
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

    notificationService.sendNotification(

            complaint.getUserMobile(),

            "Complaint Status Updated",

            "Complaint "

                    + complaint.getComplaintId()

                    + " updated to "

                    + newStatus
    );

    liveUpdateService.sendLiveUpdate(

            "STATUS_UPDATED",

            complaint
    );

    return Map.of(

            "success", true,

            "message",
            "Complaint updated successfully"
    );
}

@PostMapping("/field-visit/{complaintId}")
public Map<String, Object> markFieldVisit(

        @PathVariable String complaintId

) {

    Complaint complaint =
            complaintRepository.findByComplaintId(
                    complaintId
            );

    if (complaint == null) {

        return Map.of(

                "success", false,

                "message", "Complaint not found"
        );
    }

    complaint.setFieldVisited(
            true
    );

    complaint.setFieldVisitTime(
            LocalDateTime.now()
    );

    complaintRepository.save(
            complaint
    );

    ComplaintHistory history =
            new ComplaintHistory();

    history.setComplaintId(
            complaint.getComplaintId()
    );

    history.setAction(
            "FIELD_VISIT_COMPLETED"
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
            "Officer visited field location"
    );

    history.setTimestamp(
            LocalDateTime.now()
    );

    historyRepository.save(
            history
    );

    notificationService.sendNotification(

            complaint.getUserMobile(),

            "Field Visit Completed",

            "Officer has visited your complaint location."
    );

    liveUpdateService.sendLiveUpdate(

            "FIELD_VISIT_COMPLETED",

            complaint
    );

    return Map.of(

            "success", true,

            "message",
            "Field visit marked successfully"
    );
}


}
