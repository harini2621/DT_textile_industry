package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(String status);

    List<Task> findByDepartment(String department);

    List<Task> findByAssignedWorker(String assignedWorker);

    List<Task> findByAssignedWorkerAndStatus(String assignedWorker, String status);

    // Dashboard counts: avoid loading every task row just to count them.
    long countByStatus(String status);

    long countByAssignedWorker(String assignedWorker);

    long countByAssignedWorkerAndStatus(String assignedWorker, String status);
}
