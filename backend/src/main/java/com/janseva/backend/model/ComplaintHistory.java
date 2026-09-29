package com.janseva.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "complaint_history")
public class ComplaintHistory {


@Id
private String id;

private String complaintId;

private String action;

private String level;

private String officerName;

private String officerContact;

private String remarks;

private LocalDateTime timestamp;

public ComplaintHistory() {
}

public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public String getComplaintId() {
    return complaintId;
}

public void setComplaintId(String complaintId) {
    this.complaintId = complaintId;
}

public String getAction() {
    return action;
}

public void setAction(String action) {
    this.action = action;
}

public String getLevel() {
    return level;
}

public void setLevel(String level) {
    this.level = level;
}

public String getOfficerName() {
    return officerName;
}

public void setOfficerName(String officerName) {
    this.officerName = officerName;
}

public String getOfficerContact() {
    return officerContact;
}

public void setOfficerContact(String officerContact) {
    this.officerContact = officerContact;
}

public String getRemarks() {
    return remarks;
}

public void setRemarks(String remarks) {
    this.remarks = remarks;
}

public LocalDateTime getTimestamp() {
    return timestamp;
}

public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
}


}
