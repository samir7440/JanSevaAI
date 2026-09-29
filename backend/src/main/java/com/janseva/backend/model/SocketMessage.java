package com.janseva.backend.model;

import java.time.LocalDateTime;

public class SocketMessage {


private String type;

private String sender;

private String receiver;

private String complaintId;

private String message;

private LocalDateTime timestamp;

public SocketMessage() {
}

public String getType() {
    return type;
}

public void setType(String type) {
    this.type = type;
}

public String getSender() {
    return sender;
}

public void setSender(String sender) {
    this.sender = sender;
}

public String getReceiver() {
    return receiver;
}

public void setReceiver(String receiver) {
    this.receiver = receiver;
}

public String getComplaintId() {
    return complaintId;
}

public void setComplaintId(String complaintId) {
    this.complaintId = complaintId;
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

public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
}


}
