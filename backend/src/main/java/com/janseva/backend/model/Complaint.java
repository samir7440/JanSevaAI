package com.janseva.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

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

    private String priority;

    private String currentLevel;

    private int levelIndex;

    private String assignedPersonName;

    private String assignedPersonContact;

    private String serviceOtp;

    private double latitude;

    private double longitude;

    private String address;

    // =========================
    // GOVERNANCE FIELDS
    // =========================

    private String areaType;

    private String village;

    private String block;

    private String district;

    private String state;

    private String city;

    private String ward;

    // =========================

    private boolean fieldVisited;

    private LocalDateTime fieldVisitTime;

    private boolean fakeComplaint;

    private double aiConfidenceScore;

    private List<String> mediaUrls;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastEscalationAt;

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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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

    public void setAssignedPersonName(String assignedPersonName) {
        this.assignedPersonName = assignedPersonName;
    }

    public String getAssignedPersonContact() {
        return assignedPersonContact;
    }

    public void setAssignedPersonContact(String assignedPersonContact) {
        this.assignedPersonContact = assignedPersonContact;
    }

    public String getServiceOtp() {
        return serviceOtp;
    }

    public void setServiceOtp(String serviceOtp) {
        this.serviceOtp = serviceOtp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // =========================
    // GOVERNANCE GETTERS/SETTERS
    // =========================

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    // =========================

    public boolean isFieldVisited() {
        return fieldVisited;
    }

    public void setFieldVisited(boolean fieldVisited) {
        this.fieldVisited = fieldVisited;
    }

    public LocalDateTime getFieldVisitTime() {
        return fieldVisitTime;
    }

    public void setFieldVisitTime(LocalDateTime fieldVisitTime) {
        this.fieldVisitTime = fieldVisitTime;
    }

    public boolean isFakeComplaint() {
        return fakeComplaint;
    }

    public void setFakeComplaint(boolean fakeComplaint) {
        this.fakeComplaint = fakeComplaint;
    }

    public double getAiConfidenceScore() {
        return aiConfidenceScore;
    }

    public void setAiConfidenceScore(double aiConfidenceScore) {
        this.aiConfidenceScore = aiConfidenceScore;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastEscalationAt() {
        return lastEscalationAt;
    }

    public void setLastEscalationAt(LocalDateTime lastEscalationAt) {
        this.lastEscalationAt = lastEscalationAt;
    }
}