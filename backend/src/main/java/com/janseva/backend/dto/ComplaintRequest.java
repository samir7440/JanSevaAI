package com.janseva.backend.dto;

public class ComplaintRequest {

    private String mainCategory;

    private String subCategory;

    private String description;

    private String userMobile;

    private double latitude;

    private double longitude;

    private String userId;

    /*
     * =========================
     * GOVERNANCE FIELDS
     * =========================
     */

    private String areaType;

    private String village;

    private String block;

    private String district;

    private String state;

    private String city;

    private String ward;

    public ComplaintRequest() {
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(
            String mainCategory
    ) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(
            String subCategory
    ) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description
    ) {
        this.description = description;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(
            String userMobile
    ) {
        this.userMobile = userMobile;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(
            double latitude
    ) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(
            double longitude
    ) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(
            String userId
    ) {
        this.userId = userId;
    }

    /*
     * =========================
     * GOVERNANCE GETTERS/SETTERS
     * =========================
     */

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(
            String areaType
    ) {
        this.areaType = areaType;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(
            String village
    ) {
        this.village = village;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(
            String block
    ) {
        this.block = block;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(
            String district
    ) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(
            String state
    ) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(
            String city
    ) {
        this.city = city;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(
            String ward
    ) {
        this.ward = ward;
    }
}