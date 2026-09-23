package com.textile.smart_textile_tracking_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(nullable = false)
    private String action;

    private String performedBy;

    @Column(length = 500)
    private String details;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ActivityLog() {
    }

    public ActivityLog(String action, String performedBy, String details) {
        this.action = action;
        this.performedBy = performedBy;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
