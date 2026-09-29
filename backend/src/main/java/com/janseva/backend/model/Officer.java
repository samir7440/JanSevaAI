package com.janseva.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "officers")
public class Officer {

    @Id
    private String id;

    private String name;

    private String department;

    private String level;

    private String mobile;

    private boolean available;

    public Officer() {
    }

    public Officer(
            String id,
            String name,
            String department,
            String level,
            String mobile,
            boolean available
    ) {

        this.id = id;
        this.name = name;
        this.department = department;
        this.level = level;
        this.mobile = mobile;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}