package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.Task;
import com.textile.smart_textile_tracking_system.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Save Task
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    // Get All Tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get Task By ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Get Tasks By Status
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    // Get Tasks By Department
    public List<Task> getTasksByDepartment(String department) {
        return taskRepository.findByDepartment(department);
    }

    // Get Tasks By Assigned Worker
    public List<Task> getTasksByAssignedWorker(String assignedWorker) {
        return taskRepository.findByAssignedWorker(assignedWorker);
    }

    // Delete Task
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}