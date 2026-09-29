package janseva.dashboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "complaints")
public class Complaint {

    @Id
    private String id;

    private String complaintId;

    private String mainCategory;

    private String subCategory;

    private String description;

    private String userMobile;

    private String status;

    private String currentLevel;

    private int levelIndex;

    private String assignedPersonName;

    private String assignedPersonContact;

    private String serviceOtp;

    // NEW DATE TIME FIELD
    private LocalDateTime createdAt;

    public Complaint() {
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

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

    public String getAssignedPersonName() {
        return assignedPersonName;
    }

    public void setAssignedPersonName(
            String assignedPersonName
    ) {
        this.assignedPersonName =
                assignedPersonName;
    }

    public String getAssignedPersonContact() {
        return assignedPersonContact;
    }

    public void setAssignedPersonContact(
            String assignedPersonContact
    ) {
        this.assignedPersonContact =
                assignedPersonContact;
    }

    public String getServiceOtp() {
        return serviceOtp;
    }

    public void setServiceOtp(String serviceOtp) {
        this.serviceOtp = serviceOtp;
    }

    // DATE TIME GETTER SETTER

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }
}