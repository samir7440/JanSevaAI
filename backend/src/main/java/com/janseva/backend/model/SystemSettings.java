package com.janseva.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "system_settings")
public class SystemSettings {


@Id
private String id;

private boolean complaintSubmissionEnabled;

private boolean aiModerationEnabled;

private boolean escalationEnabled;

private boolean liveTrackingEnabled;

public SystemSettings() {
}

public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public boolean isComplaintSubmissionEnabled() {
    return complaintSubmissionEnabled;
}

public void setComplaintSubmissionEnabled(boolean complaintSubmissionEnabled) {
    this.complaintSubmissionEnabled = complaintSubmissionEnabled;
}

public boolean isAiModerationEnabled() {
    return aiModerationEnabled;
}

public void setAiModerationEnabled(boolean aiModerationEnabled) {
    this.aiModerationEnabled = aiModerationEnabled;
}

public boolean isEscalationEnabled() {
    return escalationEnabled;
}

public void setEscalationEnabled(boolean escalationEnabled) {
    this.escalationEnabled = escalationEnabled;
}

public boolean isLiveTrackingEnabled() {
    return liveTrackingEnabled;
}

public void setLiveTrackingEnabled(boolean liveTrackingEnabled) {
    this.liveTrackingEnabled = liveTrackingEnabled;
}


}
