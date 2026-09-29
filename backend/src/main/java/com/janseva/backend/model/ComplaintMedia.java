package com.janseva.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "complaint_media")
public class ComplaintMedia {


@Id
private String id;

private String complaintId;

private String fileName;

private String fileType;

private String mediaUrl;

private String uploadedBy;

private LocalDateTime uploadedAt;

public ComplaintMedia() {
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

public String getFileName() {
    return fileName;
}

public void setFileName(String fileName) {
    this.fileName = fileName;
}

public String getFileType() {
    return fileType;
}

public void setFileType(String fileType) {
    this.fileType = fileType;
}

public String getMediaUrl() {
    return mediaUrl;
}

public void setMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
}

public String getUploadedBy() {
    return uploadedBy;
}

public void setUploadedBy(String uploadedBy) {
    this.uploadedBy = uploadedBy;
}

public LocalDateTime getUploadedAt() {
    return uploadedAt;
}

public void setUploadedAt(
        LocalDateTime uploadedAt
) {
    this.uploadedAt = uploadedAt;
}


}
