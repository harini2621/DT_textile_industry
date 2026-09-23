package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.ActivityLog;
import com.textile.smart_textile_tracking_system.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    /**
     * Records an activity. Never throws so logging can never break a business flow.
     */
    public void log(String action, String performedBy, String details) {
        try {
            activityLogRepository.save(new ActivityLog(action, performedBy, details));
        } catch (Exception ignored) {
            // Logging must not interrupt the main operation.
        }
    }

    public List<ActivityLog> getAllLogs() {
        return activityLogRepository.findAllByOrderByTimestampDesc();
    }

    public List<ActivityLog> getLogsByAction(String action) {
        return activityLogRepository.findByAction(action);
    }

    public List<ActivityLog> getLogsByUser(String performedBy) {
        return activityLogRepository.findByPerformedBy(performedBy);
    }

    public long getTotalLogs() {
        return activityLogRepository.count();
    }
}
