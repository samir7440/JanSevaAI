package com.janseva.backend.service;

import com.janseva.backend.model.Complaint;
import com.janseva.backend.model.ComplaintHistory;
import com.janseva.backend.model.Officer;

import com.janseva.backend.repository.ComplaintHistoryRepository;
import com.janseva.backend.repository.ComplaintRepository;
import com.janseva.backend.repository.OfficerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EscalationService {


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

@Scheduled(fixedRate = 60000)
public void checkAndEscalate() {

    List<Complaint> complaints =
            complaintRepository.findAll();

    for (Complaint complaint : complaints) {

        if (

                complaint.getStatus() != null

                        &&

                        complaint.getStatus()
                                .equalsIgnoreCase(
                                        "RESOLVED"
                                )
        ) {

            continue;
        }

        if (

                complaint.getPriority() != null

                        &&

                        complaint.getPriority()
                                .equalsIgnoreCase(
                                        "CRITICAL"
                                )
        ) {

            if (

                    complaint.getLastEscalationAt()

                            .isBefore(

                                    LocalDateTime.now()
                                            .minusMinutes(5)
                            )
            ) {

                escalateComplaint(
                        complaint
                );
            }

            continue;
        }

        if (
        complaint.getLastEscalationAt()
                == null
) {

    complaint.setLastEscalationAt(
            LocalDateTime.now()
    );

    complaintRepository.save(
            complaint
    );

    continue;
}

        boolean shouldEscalate =

                complaint.getLastEscalationAt()

                        .isBefore(

                                LocalDateTime.now()

                                        .minusHours(24)
                        );

        if (shouldEscalate) {

            escalateComplaint(
                    complaint
            );
        }
    }
}

private void escalateComplaint(
        Complaint complaint
) {

    String currentLevel =
            complaint.getCurrentLevel();

    String nextLevel =
            getNextLevel(currentLevel);

    if (nextLevel == null) {

        complaint.setStatus(
                "PENDING_CENTRAL_REVIEW"
        );

        complaintRepository.save(
                complaint
        );

        return;
    }

    Officer nextOfficer =

            officerRepository
                    .findFirstByDepartmentIgnoreCaseAndLevelIgnoreCaseAndAvailableTrue(

                            complaint.getMainCategory(),

                            nextLevel
                    );

    if (nextOfficer == null) {

        nextOfficer =

                officerRepository
                        .findFirstByDepartmentIgnoreCaseAndLevelIgnoreCaseAndAvailableTrue(

                                "Other",

                                nextLevel
                        );
    }

    complaint.setCurrentLevel(
            nextLevel
    );

    complaint.setLevelIndex(
            complaint.getLevelIndex() + 1
    );

    complaint.setStatus(
            "ESCALATED"
    );

    complaint.setLastEscalationAt(
            LocalDateTime.now()
    );

    if (nextOfficer != null) {

        complaint.setAssignedPersonName(
                nextOfficer.getName()
        );

        complaint.setAssignedPersonContact(
                nextOfficer.getMobile()
        );

    } else {

        complaint.setAssignedPersonName(
                "NOT_ASSIGNED"
        );

        complaint.setAssignedPersonContact(
                "N/A"
        );
    }

    Complaint updated =
            complaintRepository.save(
                    complaint
            );

    saveEscalationHistory(
            updated
    );

    notificationService.sendNotification(

            updated.getAssignedPersonContact(),

            "Complaint Escalated",

            "Complaint "

                    + updated.getComplaintId()

                    + " escalated to your level."
    );

    notificationService.sendNotification(

            updated.getUserMobile(),

            "Complaint Escalated",

            "Your complaint "

                    + updated.getComplaintId()

                    + " has been escalated to higher authority."
    );

    liveUpdateService.sendLiveUpdate(

            "COMPLAINT_ESCALATED",

            updated
    );
}

private void saveEscalationHistory(
        Complaint complaint
) {

    ComplaintHistory history =
            new ComplaintHistory();

    history.setComplaintId(
            complaint.getComplaintId()
    );

    history.setAction(
            "COMPLAINT_ESCALATED"
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
            "Complaint escalated automatically"
    );

    history.setTimestamp(
            LocalDateTime.now()
    );

    historyRepository.save(
            history
    );
}

private String getNextLevel(
        String currentLevel
) {

    if (currentLevel == null) {
        return null;
    }

    return switch (
            currentLevel.toUpperCase()
    ) {

        case "VILLAGE" -> "BLOCK";

        case "BLOCK" -> "DISTRICT";

        case "WARD" -> "ZONE";

        case "ZONE" -> "STATE";

        case "DISTRICT" -> "STATE";

        case "STATE" -> "CENTRAL";

        default -> null;
    };
}


}
