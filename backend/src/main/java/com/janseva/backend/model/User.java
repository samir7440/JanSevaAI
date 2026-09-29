package com.janseva.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
public class User {


@Id
private String id;

private String fullName;

private String mobile;

private String password;

private String role;

private boolean active;

private LocalDateTime createdAt;

public User() {
}

public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public String getFullName() {
    return fullName;
}

public void setFullName(String fullName) {
    this.fullName = fullName;
}

public String getMobile() {
    return mobile;
}

public void setMobile(String mobile) {
    this.mobile = mobile;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public String getRole() {
    return role;
}

public void setRole(String role) {
    this.role = role;
}

public boolean isActive() {
    return active;
}

public void setActive(boolean active) {
    this.active = active;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(
        LocalDateTime createdAt
) {
    this.createdAt = createdAt;
}


}
