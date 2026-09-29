package com.janseva.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
public class Message {

@Id
private String id;

private String complaintId;

private String senderRole;

private String senderName;

private String message;

private LocalDateTime timestamp;

public Message() {
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

public String getSenderRole() {
    return senderRole;
}

public void setSenderRole(String senderRole) {
    this.senderRole = senderRole;
}

public String getSenderName() {
    return senderName;
}

public void setSenderName(String senderName) {
    this.senderName = senderName;
}

public String getMessage() {
    return message;
}

public void setMessage(String message) {
    this.message = message;
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
