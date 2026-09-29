package com.janseva.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "internal_notes")
public class InternalNote {


@Id
private String id;

private String complaintId;

private String officerName;

private String note;

private LocalDateTime timestamp;

public InternalNote() {
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

public String getOfficerName() {
    return officerName;
}

public void setOfficerName(String officerName) {
    this.officerName = officerName;
}

public String getNote() {
    return note;
}

public void setNote(String note) {
    this.note = note;
}

public LocalDateTime getTimestamp() {
    return timestamp;
}

public void setTimestamp(
        LocalDateTime timestamp
) {
    this.timestamp = timestamp;
}


}
