package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findAllByOrderByTimestampDesc();

    List<ActivityLog> findByAction(String action);

    List<ActivityLog> findByPerformedBy(String performedBy);
}
